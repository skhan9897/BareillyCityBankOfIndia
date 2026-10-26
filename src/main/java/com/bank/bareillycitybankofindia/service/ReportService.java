package com.bank.bareillycitybankofindia.service;

import com.bank.bareillycitybankofindia.model.Account;
import com.bank.bareillycitybankofindia.model.Transfer;

import java.io.OutputStream;
import java.util.List;

public interface ReportService {
    void generateAccountStatementPdf(Account account, List<Transfer> transactions, OutputStream out) throws Exception;
    void generateTransactionsCsv(List<Transfer> transactions, OutputStream out) throws Exception;
}
