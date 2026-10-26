package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.model.*;
import com.bank.bareillycitybankofindia.service.*;
import com.bank.bareillycitybankofindia.service.impl.*;
import com.bank.bareillycitybankofindia.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(urlPatterns = {"/customer/transfer", "/customer/beneficiaries"})
public class TransferServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private final TransferService transferService = new TransferServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        String path = req.getServletPath();
        if ("/customer/beneficiaries".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/customer/beneficiaries.jsp").forward(req, resp);
        } else {
            List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
            req.setAttribute("accounts", accounts);
            req.getRequestDispatcher("/WEB-INF/views/customer/transfer.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        String path = req.getServletPath();
        boolean isAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With")) || path.endsWith(".json");

        try {
            if ("/customer/transfer".equals(path)) {
                long fromAccId = Long.parseLong(req.getParameter("fromAccountId"));
                String toAccNum = req.getParameter("toAccountNumber");
                BigDecimal amount = new BigDecimal(req.getParameter("amount"));
                String desc = req.getParameter("description");
                String idempotencyKey = req.getParameter("idempotencyKey");

                Account toAcc = accountService.getAccountByNumber(toAccNum);
                if (toAcc == null) {
                    throw new IllegalArgumentException("Destination account number not found.");
                }

                Transfer tx = transferService.executeTransfer(fromAccId, toAcc.getId(), amount, desc, user.getId(), idempotencyKey);

                if (isAjax) {
                    JsonUtil.sendJsonResponse(resp, 200, true, "Transfer completed successfully!", tx);
                } else {
                    resp.sendRedirect(req.getContextPath() + "/customer/transactions?ref=" + tx.getTransactionRef());
                }
            }
        } catch (Exception e) {
            if (isAjax) {
                JsonUtil.sendJsonResponse(resp, 400, false, e.getMessage(), null);
            } else {
                req.setAttribute("errorMessage", e.getMessage());
                doGet(req, resp);
            }
        }
    }
}
