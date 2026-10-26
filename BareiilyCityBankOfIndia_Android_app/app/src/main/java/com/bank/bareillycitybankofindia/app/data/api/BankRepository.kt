package com.bank.bareillycitybankofindia.app.data.api

import com.bank.bareillycitybankofindia.app.data.model.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class BankRepository {

    // Mock Live In-Memory State
    private var loggedInUser = User(1, "customer1", "customer1@bareillybank.com", "9876543210", "CUSTOMER", "ACTIVE")
    private var currentCustomer = Customer(1, 1, "CUST1792667659613", "Asgar", "Khan", "9876543210", "asgar.khan@gmail.com", "APPROVED", "Civil Lines", "Bareilly", "Uttar Pradesh")
    
    private val accountsList = mutableListOf(
        Account(
            id = 101,
            accountNumber = "1000139854769",
            customerId = 1,
            productName = "Zero Balance Basic Savings Account (BSBD)",
            accountType = "SAVINGS",
            availableBalance = 25000.00,
            ledgerBalance = 25000.00,
            ifscCode = "BCBI0000001",
            branchName = "Civil Lines Main Branch",
            status = "ACTIVE"
        )
    )

    private val transactionsList = mutableListOf(
        Transaction(1, "DEP17926689001", null, 101, "DEPOSIT", 25000.00, "SUCCESS", "Initial Cash Deposit at Branch", "26-Oct-2026 04:30 PM"),
        Transaction(2, "TXN17926689002", 101, 102, "TRANSFER", 5000.00, "SUCCESS", "Fund Transfer to Rahul Kumar", "26-Oct-2026 05:15 PM")
    )

    private val notificationsList = mutableListOf(
        BankNotification(
            id = 1,
            title = "Cash Credit Alert: ₹ 25,000.00 Received in A/c ...4769",
            message = "Your A/c ...4769 has been Credited with ₹ 25,000.00 on 26-Oct-2026 04:30 PM via Cash Deposit at Civil Lines Main Branch (BCBI0000001). Ref: DEP17926689001. Avail Bal: ₹ 25,000.00. - BAREILLY CITY BANK OF INDIA",
            type = "SUCCESS",
            createdAt = "26-Oct-2026 04:30 PM"
        )
    )

    private val virtualCard = VirtualCard(1, 101, "4532 •••• •••• 9821", "DEBIT", 12, 2028, 50000.00, "ACTIVE")

    suspend fun login(username: String, pword: String): Result<User> {
        delay(1200) // Simulated network latency
        if (username.isBlank() || pword.isBlank()) {
            return Result.failure(Exception("Please enter valid username and password."))
        }
        return Result.success(loggedInUser)
    }

    suspend fun registerCustomer(
        username: String,
        email: String,
        mobile: String,
        pword: String,
        firstName: String,
        lastName: String,
        aadhaar: String,
        pan: String,
        address: String
    ): Result<User> {
        delay(1500)
        currentCustomer = Customer(
            id = System.currentTimeMillis(),
            userId = System.currentTimeMillis(),
            customerNumber = "CUST" + System.currentTimeMillis(),
            firstName = firstName,
            lastName = lastName,
            mobile = mobile,
            email = email,
            kycStatus = "PENDING",
            addressLine1 = address
        )
        loggedInUser = User(currentCustomer.userId, username, email, mobile, "CUSTOMER", "ACTIVE")
        return Result.success(loggedInUser)
    }

    suspend fun getCustomerProfile(): Customer = currentCustomer

    suspend fun getAccounts(): List<Account> {
        delay(400)
        return accountsList
    }

    suspend fun getTransactions(): List<Transaction> {
        delay(300)
        return transactionsList
    }

    suspend fun getNotifications(): List<BankNotification> {
        delay(300)
        return notificationsList
    }

    suspend fun getVirtualCard(): VirtualCard = virtualCard

    suspend fun transferFunds(
        fromAccountId: Long,
        toAccountNum: String,
        amount: Double,
        description: String
    ): Result<Transaction> {
        delay(2000) // 2-second processing
        val acc = accountsList.find { it.id == fromAccountId } ?: accountsList.first()
        if (acc.availableBalance < amount) {
            return Result.failure(Exception("Insufficient available balance. Your available balance is ₹ ${acc.availableBalance}"))
        }

        // Debit balance
        val newBal = acc.availableBalance - amount
        val updatedAcc = acc.copy(availableBalance = newBal, ledgerBalance = newBal)
        accountsList[0] = updatedAcc

        val timeStr = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault()).format(Date())
        val txRef = "TXN" + System.currentTimeMillis()

        val tx = Transaction(
            id = System.currentTimeMillis(),
            transactionRef = txRef,
            fromAccountId = fromAccountId,
            transactionType = "TRANSFER",
            amount = amount,
            status = "SUCCESS",
            description = if (description.isBlank()) "Fund Transfer to $toAccountNum" else description,
            createdAt = timeStr
        )
        transactionsList.add(0, tx)

        // Add SMS alert notification
        val maskAcc = if (acc.accountNumber.length > 4) "..." + acc.accountNumber.takeLast(4) else acc.accountNumber
        val notif = BankNotification(
            id = System.currentTimeMillis(),
            title = "Debit Alert: ₹ $amount Debited from A/c $maskAcc",
            message = "Your A/c $maskAcc has been Debited with ₹ $amount on $timeStr transferred to $toAccountNum. Ref: $txRef. Avail Bal: ₹ $newBal. - BAREILLY CITY BANK OF INDIA",
            type = "INFO",
            createdAt = timeStr
        )
        notificationsList.add(0, notif)

        return Result.success(tx)
    }

    companion object {
        val instance by lazy { BankRepository() }
    }
}
