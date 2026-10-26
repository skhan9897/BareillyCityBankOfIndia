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

@WebServlet(urlPatterns = {"/customer/deposits", "/customer/fd-calc", "/customer/create-fd", "/customer/create-rd", "/customer/close-fd"})
public class DepositServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private final DepositService depositService = new DepositServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        String path = req.getServletPath();
        if ("/customer/fd-calc".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/customer/fd-calculator.jsp").forward(req, resp);
        } else {
            List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
            List<FixedDeposit> fds = null;
            List<RecurringDeposit> rds = null;
            if (!accounts.isEmpty()) {
                long primaryAccId = accounts.get(0).getId();
                fds = depositService.getFdsByAccountId(primaryAccId);
                rds = depositService.getRdsByAccountId(primaryAccId);
            }
            req.setAttribute("accounts", accounts);
            req.setAttribute("fds", fds);
            req.setAttribute("rds", rds);
            req.getRequestDispatcher("/WEB-INF/views/customer/deposits.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");

        String path = req.getServletPath();
        boolean isAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With")) || path.endsWith(".json");

        try {
            if ("/customer/create-fd".equals(path)) {
                long accId = Long.parseLong(req.getParameter("accountId"));
                BigDecimal amount = new BigDecimal(req.getParameter("principalAmount"));
                int tenure = Integer.parseInt(req.getParameter("tenureMonths"));

                FixedDeposit fd = depositService.createFixedDeposit(accId, amount, tenure, user.getId());
                JsonUtil.sendJsonResponse(resp, 200, true, "Fixed Deposit created successfully!", fd);

            } else if ("/customer/create-rd".equals(path)) {
                long accId = Long.parseLong(req.getParameter("accountId"));
                BigDecimal installment = new BigDecimal(req.getParameter("monthlyInstallment"));
                int tenure = Integer.parseInt(req.getParameter("tenureMonths"));

                RecurringDeposit rd = depositService.createRecurringDeposit(accId, installment, tenure, user.getId());
                JsonUtil.sendJsonResponse(resp, 200, true, "Recurring Deposit created successfully!", rd);

            } else if ("/customer/close-fd".equals(path)) {
                long fdId = Long.parseLong(req.getParameter("fdId"));
                depositService.prematureCloseFd(fdId, user.getId());
                JsonUtil.sendJsonResponse(resp, 200, true, "Fixed Deposit closed prematurely and funds refunded.", null);
            }
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, 400, false, e.getMessage(), null);
        }
    }
}
