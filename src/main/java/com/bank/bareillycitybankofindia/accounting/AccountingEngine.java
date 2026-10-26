package com.bank.bareillycitybankofindia.accounting;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AccountingEngine {
    private static final Logger logger = LoggerFactory.getLogger(AccountingEngine.class);

    // Ledger Account IDs from Chart of Accounts (seed.sql)
    public static final int VAULT_CASH = 1;
    public static final int INTERBANK_CLEARING = 2;
    public static final int LOANS_RECEIVABLE = 3;
    public static final int CUSTOMER_SAVINGS = 4;
    public static final int CUSTOMER_CURRENT = 5;
    public static final int FIXED_DEPOSITS = 6;
    public static final int RECURRING_DEPOSITS = 7;
    public static final int BANK_CAPITAL = 8;
    public static final int INTEREST_INCOME = 9;
    public static final int FEE_INCOME = 10;
    public static final int INTEREST_EXPENSE = 11;

    private static class AccountDetails {
        String accountNumber;
        BigDecimal availableBalance;
        long userId;
        String fullName;
        String branchName;
        String ifscCode;
    }

    private static AccountDetails lookupAccountDetails(Connection conn, long accountId) throws Exception {
        String sql = "SELECT a.account_number, a.available_balance, c.user_id, CONCAT(c.first_name, ' ', c.last_name) AS full_name, b.branch_name, b.ifsc_code " +
                     "FROM accounts a " +
                     "JOIN customers c ON a.customer_id = c.id " +
                     "JOIN branches b ON a.branch_id = b.id " +
                     "WHERE a.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AccountDetails ad = new AccountDetails();
                    ad.accountNumber = rs.getString("account_number");
                    ad.availableBalance = rs.getBigDecimal("available_balance");
                    ad.userId = rs.getLong("user_id");
                    ad.fullName = rs.getString("full_name");
                    ad.branchName = rs.getString("branch_name");
                    ad.ifscCode = rs.getString("ifsc_code");
                    return ad;
                }
            }
        }
        return null;
    }

    private static void recordNotification(Connection conn, long userId, String title, String message, String type) {
        String sql = "INSERT INTO notifications (user_id, title, message, type, is_read) VALUES (?, ?, ?, ?, false)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setString(2, title);
            ps.setString(3, message);
            ps.setString(4, type);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Failed to record notification: " + e.getMessage());
        }
    }

    public static Transfer processDeposit(long accountId, BigDecimal amount, String description, long initiatedBy) throws Exception {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String lock = "SELECT available_balance, status, account_type FROM accounts WHERE id = ? FOR UPDATE";
                BigDecimal currentBal;
                String status, type;
                try (PreparedStatement ps = conn.prepareStatement(lock)) {
                    ps.setLong(1, accountId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Account not found.");
                        currentBal = rs.getBigDecimal("available_balance");
                        status = rs.getString("status");
                        type = rs.getString("account_type");
                    }
                }

                if (!"ACTIVE".equalsIgnoreCase(status)) {
                    throw new IllegalStateException("Account is not ACTIVE (Status: " + status + ").");
                }

                BigDecimal newBal = currentBal.add(amount);
                String update = "UPDATE accounts SET available_balance = ?, ledger_balance = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(update)) {
                    ps.setBigDecimal(1, newBal);
                    ps.setBigDecimal(2, newBal);
                    ps.setLong(3, accountId);
                    ps.executeUpdate();
                }

                String refNo = "JE-DEP-" + System.currentTimeMillis();
                int toLedgerAcc = "SAVINGS".equalsIgnoreCase(type) ? CUSTOMER_SAVINGS : CUSTOMER_CURRENT;
                long jeId = recordJournalEntry(conn, refNo, description, initiatedBy, VAULT_CASH, toLedgerAcc, amount);

                String txRef = "DEP" + System.currentTimeMillis() + (int)(Math.random() * 1000);
                String insertTx = "INSERT INTO transactions (transaction_ref, to_account_id, journal_entry_id, transaction_type, amount, status, description, initiated_by) VALUES (?, ?, ?, 'DEPOSIT', ?, 'SUCCESS', ?, ?)";
                long txId;
                try (PreparedStatement ps = conn.prepareStatement(insertTx, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, txRef);
                    ps.setLong(2, accountId);
                    ps.setLong(3, jeId);
                    ps.setBigDecimal(4, amount);
                    ps.setString(5, description);
                    ps.setLong(6, initiatedBy);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        txId = rs.getLong(1);
                    }
                }

                // Credit Notification SMS Alert
                String timeStr = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(new Date());
                AccountDetails toAccDetails = lookupAccountDetails(conn, accountId);
                if (toAccDetails != null) {
                    String maskAcc = toAccDetails.accountNumber.length() > 4 ? "..." + toAccDetails.accountNumber.substring(toAccDetails.accountNumber.length() - 4) : toAccDetails.accountNumber;
                    String title = "Cash Credit Alert: ₹ " + String.format("%,.2f", amount) + " Received in A/c " + maskAcc;
                    String msg = "Your A/c " + maskAcc + " has been Credited with ₹ " + String.format("%,.2f", amount) + " on " + timeStr + " via Cash Deposit at " + toAccDetails.branchName + " (" + toAccDetails.ifscCode + "). Ref: " + txRef + ". Avail Bal: ₹ " + String.format("%,.2f", newBal) + ". - BAREILLY CITY BANK OF INDIA";
                    recordNotification(conn, toAccDetails.userId, title, msg, "SUCCESS");
                }

                conn.commit();

                Transfer tx = new Transfer();
                tx.setId(txId);
                tx.setTransactionRef(txRef);
                tx.setToAccountId(accountId);
                tx.setTransactionType("DEPOSIT");
                tx.setAmount(amount);
                tx.setStatus("SUCCESS");
                tx.setDescription(description);
                return tx;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public static Transfer processWithdrawal(long accountId, BigDecimal amount, String description, long initiatedBy) throws Exception {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String lock = "SELECT available_balance, status, account_type FROM accounts WHERE id = ? FOR UPDATE";
                BigDecimal currentBal;
                String status, type;
                try (PreparedStatement ps = conn.prepareStatement(lock)) {
                    ps.setLong(1, accountId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Account not found.");
                        currentBal = rs.getBigDecimal("available_balance");
                        status = rs.getString("status");
                        type = rs.getString("account_type");
                    }
                }

                if (!"ACTIVE".equalsIgnoreCase(status)) {
                    throw new IllegalStateException("Account is not ACTIVE (Status: " + status + ").");
                }
                if (currentBal.compareTo(amount) < 0) {
                    throw new IllegalStateException("Insufficient available balance.");
                }

                BigDecimal newBal = currentBal.subtract(amount);
                String update = "UPDATE accounts SET available_balance = ?, ledger_balance = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(update)) {
                    ps.setBigDecimal(1, newBal);
                    ps.setBigDecimal(2, newBal);
                    ps.setLong(3, accountId);
                    ps.executeUpdate();
                }

                String refNo = "JE-WTH-" + System.currentTimeMillis();
                int fromLedgerAcc = "SAVINGS".equalsIgnoreCase(type) ? CUSTOMER_SAVINGS : CUSTOMER_CURRENT;
                long jeId = recordJournalEntry(conn, refNo, description, initiatedBy, fromLedgerAcc, VAULT_CASH, amount);

                String txRef = "WTH" + System.currentTimeMillis() + (int)(Math.random() * 1000);
                String insertTx = "INSERT INTO transactions (transaction_ref, from_account_id, journal_entry_id, transaction_type, amount, status, description, initiated_by) VALUES (?, ?, ?, 'WITHDRAWAL', ?, 'SUCCESS', ?, ?)";
                long txId;
                try (PreparedStatement ps = conn.prepareStatement(insertTx, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, txRef);
                    ps.setLong(2, accountId);
                    ps.setLong(3, jeId);
                    ps.setBigDecimal(4, amount);
                    ps.setString(5, description);
                    ps.setLong(6, initiatedBy);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        txId = rs.getLong(1);
                    }
                }

                // Debit Notification SMS Alert
                String timeStr = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(new Date());
                AccountDetails accDetails = lookupAccountDetails(conn, accountId);
                if (accDetails != null) {
                    String maskAcc = accDetails.accountNumber.length() > 4 ? "..." + accDetails.accountNumber.substring(accDetails.accountNumber.length() - 4) : accDetails.accountNumber;
                    String title = "Cash Debit Alert: ₹ " + String.format("%,.2f", amount) + " Debited from A/c " + maskAcc;
                    String msg = "Your A/c " + maskAcc + " has been Debited with ₹ " + String.format("%,.2f", amount) + " on " + timeStr + " via Cash Withdrawal at Teller Counter. Ref: " + txRef + ". Avail Bal: ₹ " + String.format("%,.2f", newBal) + ". - BAREILLY CITY BANK OF INDIA";
                    recordNotification(conn, accDetails.userId, title, msg, "INFO");
                }

                conn.commit();

                Transfer tx = new Transfer();
                tx.setId(txId);
                tx.setTransactionRef(txRef);
                tx.setFromAccountId(accountId);
                tx.setTransactionType("WITHDRAWAL");
                tx.setAmount(amount);
                tx.setStatus("SUCCESS");
                tx.setDescription(description);
                return tx;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public static Transfer processTransfer(long fromAccountId, long toAccountId, BigDecimal amount, String description, long initiatedBy, String idempotencyKey) throws Exception {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Source and destination accounts cannot be identical.");
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Check idempotency if key provided
                if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
                    String checkIdempotency = "SELECT id FROM idempotency_keys WHERE key_value = ?";
                    try (PreparedStatement ps = conn.prepareStatement(checkIdempotency)) {
                        ps.setString(1, idempotencyKey);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                throw new IllegalStateException("Duplicate transaction request detected (Idempotency key match).");
                            }
                        }
                    }
                }

                // Lock source account
                String lockFrom = "SELECT available_balance, status, account_type FROM accounts WHERE id = ? FOR UPDATE";
                BigDecimal fromBal;
                String fromStatus, fromType;
                try (PreparedStatement ps = conn.prepareStatement(lockFrom)) {
                    ps.setLong(1, fromAccountId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Source account not found.");
                        fromBal = rs.getBigDecimal("available_balance");
                        fromStatus = rs.getString("status");
                        fromType = rs.getString("account_type");
                    }
                }

                if (!"ACTIVE".equalsIgnoreCase(fromStatus)) {
                    throw new IllegalStateException("Source account is not ACTIVE (Status: " + fromStatus + ").");
                }
                if (fromBal.compareTo(amount) < 0) {
                    throw new IllegalStateException("Insufficient available balance.");
                }

                // Lock destination account
                String lockTo = "SELECT available_balance, status, account_type FROM accounts WHERE id = ? FOR UPDATE";
                BigDecimal toBal;
                String toStatus, toType;
                try (PreparedStatement ps = conn.prepareStatement(lockTo)) {
                    ps.setLong(1, toAccountId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Destination account not found.");
                        toBal = rs.getBigDecimal("available_balance");
                        toStatus = rs.getString("status");
                        toType = rs.getString("account_type");
                    }
                }

                if (!"ACTIVE".equalsIgnoreCase(toStatus)) {
                    throw new IllegalStateException("Destination account is not ACTIVE (Status: " + toStatus + ").");
                }

                // Debit Source Account
                BigDecimal newFromBal = fromBal.subtract(amount);
                String updateFrom = "UPDATE accounts SET available_balance = ?, ledger_balance = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateFrom)) {
                    ps.setBigDecimal(1, newFromBal);
                    ps.setBigDecimal(2, newFromBal);
                    ps.setLong(3, fromAccountId);
                    ps.executeUpdate();
                }

                // Credit Destination Account
                BigDecimal newToBal = toBal.add(amount);
                String updateTo = "UPDATE accounts SET available_balance = ?, ledger_balance = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateTo)) {
                    ps.setBigDecimal(1, newToBal);
                    ps.setBigDecimal(2, newToBal);
                    ps.setLong(3, toAccountId);
                    ps.executeUpdate();
                }

                // Create Journal Entry (Double-Entry Posting)
                String refNo = "JE-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6);
                int fromLedgerAcc = "SAVINGS".equalsIgnoreCase(fromType) ? CUSTOMER_SAVINGS : CUSTOMER_CURRENT;
                int toLedgerAcc = "SAVINGS".equalsIgnoreCase(toType) ? CUSTOMER_SAVINGS : CUSTOMER_CURRENT;

                long jeId = recordJournalEntry(conn, refNo, description, initiatedBy, fromLedgerAcc, toLedgerAcc, amount);

                // Record Transaction
                String txRef = "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
                String insertTx = "INSERT INTO transactions (transaction_ref, from_account_id, to_account_id, journal_entry_id, transaction_type, amount, status, description, initiated_by, idempotency_key) VALUES (?, ?, ?, ?, 'TRANSFER', ?, 'SUCCESS', ?, ?, ?)";
                long txId;
                try (PreparedStatement ps = conn.prepareStatement(insertTx, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, txRef);
                    ps.setLong(2, fromAccountId);
                    ps.setLong(3, toAccountId);
                    ps.setLong(4, jeId);
                    ps.setBigDecimal(5, amount);
                    ps.setString(6, description);
                    ps.setLong(7, initiatedBy);
                    ps.setString(8, idempotencyKey);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        txId = rs.getLong(1);
                    }
                }

                // Record Idempotency key if provided
                if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
                    String insertIdem = "INSERT INTO idempotency_keys (key_value, user_id, response_json) VALUES (?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertIdem)) {
                        ps.setString(1, idempotencyKey);
                        ps.setLong(2, initiatedBy);
                        ps.setString(3, "{\"transactionRef\":\"" + txRef + "\",\"status\":\"SUCCESS\"}");
                        ps.executeUpdate();
                    }
                }

                // Credit & Debit Instant Notifications
                String timeStr = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(new Date());
                AccountDetails fromAccDetails = lookupAccountDetails(conn, fromAccountId);
                AccountDetails toAccDetails = lookupAccountDetails(conn, toAccountId);

                if (toAccDetails != null && fromAccDetails != null) {
                    String maskToAcc = toAccDetails.accountNumber.length() > 4 ? "..." + toAccDetails.accountNumber.substring(toAccDetails.accountNumber.length() - 4) : toAccDetails.accountNumber;
                    String maskFromAcc = fromAccDetails.accountNumber.length() > 4 ? "..." + fromAccDetails.accountNumber.substring(fromAccDetails.accountNumber.length() - 4) : fromAccDetails.accountNumber;

                    // Credit Alert to Receiver
                    String credTitle = "Credit Alert: ₹ " + String.format("%,.2f", amount) + " Received from " + fromAccDetails.fullName;
                    String credMsg = "Your A/c " + maskToAcc + " has been Credited with ₹ " + String.format("%,.2f", amount) + " on " + timeStr + " received from " + fromAccDetails.fullName + " (A/c " + maskFromAcc + ", BAREILLY CITY BANK OF INDIA, IFSC: " + fromAccDetails.ifscCode + "). Ref: " + txRef + ". Avail Bal: ₹ " + String.format("%,.2f", newToBal) + ". - BAREILLY CITY BANK OF INDIA";
                    recordNotification(conn, toAccDetails.userId, credTitle, credMsg, "SUCCESS");

                    // Debit Alert to Sender
                    String debTitle = "Debit Alert: ₹ " + String.format("%,.2f", amount) + " Debited for Transfer to " + toAccDetails.fullName;
                    String debMsg = "Your A/c " + maskFromAcc + " has been Debited with ₹ " + String.format("%,.2f", amount) + " on " + timeStr + " transferred to " + toAccDetails.fullName + " (A/c " + maskToAcc + "). Ref: " + txRef + ". Avail Bal: ₹ " + String.format("%,.2f", newFromBal) + ". - BAREILLY CITY BANK OF INDIA";
                    recordNotification(conn, fromAccDetails.userId, debTitle, debMsg, "INFO");
                }

                conn.commit();

                Transfer tx = new Transfer();
                tx.setId(txId);
                tx.setTransactionRef(txRef);
                tx.setFromAccountId(fromAccountId);
                tx.setToAccountId(toAccountId);
                tx.setJournalEntryId(jeId);
                tx.setTransactionType("TRANSFER");
                tx.setAmount(amount);
                tx.setStatus("SUCCESS");
                tx.setDescription(description);
                tx.setInitiatedBy(initiatedBy);
                tx.setIdempotencyKey(idempotencyKey);
                return tx;

            } catch (Exception e) {
                conn.rollback();
                logger.error("Error processing transaction: {}", e.getMessage(), e);
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private static long recordJournalEntry(Connection conn, String refNo, String description, long postedBy, int debitLedgerAccId, int creditLedgerAccId, BigDecimal amount) throws Exception {
        String insertJE = "INSERT INTO journal_entries (reference_no, description, posted_by) VALUES (?, ?, ?)";
        long jeId;
        try (PreparedStatement ps = conn.prepareStatement(insertJE, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, refNo);
            ps.setString(2, description);
            ps.setLong(3, postedBy);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                jeId = rs.getLong(1);
            }
        }

        // Debit Line
        String insertLine = "INSERT INTO journal_entry_lines (entry_id, ledger_account_id, debit_amount, credit_amount) VALUES (?, ?, ?, 0.00)";
        try (PreparedStatement ps = conn.prepareStatement(insertLine)) {
            ps.setLong(1, jeId);
            ps.setInt(2, debitLedgerAccId);
            ps.setBigDecimal(3, amount);
            ps.executeUpdate();
        }

        // Credit Line
        String insertCreditLine = "INSERT INTO journal_entry_lines (entry_id, ledger_account_id, debit_amount, credit_amount) VALUES (?, ?, 0.00, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertCreditLine)) {
            ps.setLong(1, jeId);
            ps.setInt(2, creditLedgerAccId);
            ps.setBigDecimal(3, amount);
            ps.executeUpdate();
        }

        return jeId;
    }
}
