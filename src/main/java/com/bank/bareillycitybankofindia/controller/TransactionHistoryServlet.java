package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.model.*;
import com.bank.bareillycitybankofindia.service.*;
import com.bank.bareillycitybankofindia.service.impl.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/customer/transactions", "/customer/statement", "/customer/statement/pdf", "/customer/statement/csv"})
public class TransactionHistoryServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private final TransferService transferService = new TransferServiceImpl();
    private final ReportService reportService = new ReportServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        String path = req.getServletPath();
        List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());

        long selectedAccId = 0;
        String accIdParam = req.getParameter("accountId");
        if (accIdParam != null && !accIdParam.isEmpty()) {
            selectedAccId = Long.parseLong(accIdParam);
        } else if (!accounts.isEmpty()) {
            selectedAccId = accounts.get(0).getId();
        }

        Account selectedAccount = accountService.getAccountById(selectedAccId);
        List<Transfer> transactions = transferService.getAccountStatement(selectedAccId, 100);

        if ("/customer/statement/pdf".equals(path)) {
            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition", "attachment; filename=\"Account_Statement_" + selectedAccId + ".pdf\"");
            try {
                reportService.generateAccountStatementPdf(selectedAccount, transactions, resp.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if ("/customer/statement/csv".equals(path)) {
            resp.setContentType("text/csv");
            resp.setHeader("Content-Disposition", "attachment; filename=\"Transactions_" + selectedAccId + ".csv\"");
            try {
                reportService.generateTransactionsCsv(transactions, resp.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        req.setAttribute("accounts", accounts);
        req.setAttribute("selectedAccount", selectedAccount);
        req.setAttribute("transactions", transactions);

        if ("/customer/statement".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/customer/statement.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/customer/transactions.jsp").forward(req, resp);
        }
    }
}
