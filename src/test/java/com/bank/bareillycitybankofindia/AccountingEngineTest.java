package com.bank.bareillycitybankofindia;

import com.bank.bareillycitybankofindia.config.DatabaseInitializer;
import com.bank.bareillycitybankofindia.dao.AccountDao;
import com.bank.bareillycitybankofindia.dao.impl.AccountDaoImpl;
import com.bank.bareillycitybankofindia.model.Account;
import com.bank.bareillycitybankofindia.model.Transfer;
import com.bank.bareillycitybankofindia.service.TransferService;
import com.bank.bareillycitybankofindia.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountingEngineTest {

    private static TransferService transferService;
    private static AccountDao accountDao;

    @BeforeAll
    public static void setUp() {
        DatabaseInitializer.initialize();
        transferService = new TransferServiceImpl();
        accountDao = new AccountDaoImpl();
    }

    @Test
    @DisplayName("Test Double-Entry Fund Transfer between Accounts")
    public void testFundTransfer() throws Exception {
        Account acc1 = accountDao.findByAccountNumber("100020003001");
        Account acc2 = accountDao.findByAccountNumber("100020003003");

        assertNotNull(acc1);
        assertNotNull(acc2);

        BigDecimal initialAcc1Bal = acc1.getAvailableBalance();
        BigDecimal initialAcc2Bal = acc2.getAvailableBalance();
        BigDecimal transferAmount = BigDecimal.valueOf(5000.00);
        String idemKey = "IDEM-" + UUID.randomUUID().toString();

        Transfer tx = transferService.executeTransfer(acc1.getId(), acc2.getId(), transferAmount, "Test Transfer", 1, idemKey);

        assertNotNull(tx);
        assertEquals("SUCCESS", tx.getStatus());

        Account updatedAcc1 = accountDao.findById(acc1.getId());
        Account updatedAcc2 = accountDao.findById(acc2.getId());

        assertEquals(initialAcc1Bal.subtract(transferAmount), updatedAcc1.getAvailableBalance());
        assertEquals(initialAcc2Bal.add(transferAmount), updatedAcc2.getAvailableBalance());
    }

    @Test
    @DisplayName("Test Insufficient Funds Transfer Prevention")
    public void testInsufficientFunds() {
        Account acc1 = accountDao.findByAccountNumber("100020003001");
        Account acc2 = accountDao.findByAccountNumber("100020003003");

        BigDecimal hugeAmount = BigDecimal.valueOf(999999999.00);

        assertThrows(IllegalStateException.class, () -> {
            transferService.executeTransfer(acc1.getId(), acc2.getId(), hugeAmount, "Huge Transfer", 1, "IDEM-OVERDRAFT");
        });
    }

    @Test
    @DisplayName("Test Duplicate Idempotency Key Rejection")
    public void testIdempotency() throws Exception {
        Account acc1 = accountDao.findByAccountNumber("100020003001");
        Account acc2 = accountDao.findByAccountNumber("100020003003");

        String fixedKey = "IDEM-DUPLICATE-CHECK-12345";
        BigDecimal transferAmount = BigDecimal.valueOf(100.00);

        transferService.executeTransfer(acc1.getId(), acc2.getId(), transferAmount, "Idempotency 1", 1, fixedKey);

        assertThrows(IllegalStateException.class, () -> {
            transferService.executeTransfer(acc1.getId(), acc2.getId(), transferAmount, "Idempotency 2", 1, fixedKey);
        });
    }
}
