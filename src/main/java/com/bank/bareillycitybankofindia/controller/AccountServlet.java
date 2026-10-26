package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.model.Account;
import com.bank.bareillycitybankofindia.model.Card;
import com.bank.bareillycitybankofindia.model.Customer;
import com.bank.bareillycitybankofindia.model.Transfer;
import com.bank.bareillycitybankofindia.model.User;
import com.bank.bareillycitybankofindia.service.AccountService;
import com.bank.bareillycitybankofindia.service.CardService;
import com.bank.bareillycitybankofindia.service.CustomerService;
import com.bank.bareillycitybankofindia.service.TransferService;
import com.bank.bareillycitybankofindia.service.impl.AccountServiceImpl;
import com.bank.bareillycitybankofindia.service.impl.CardServiceImpl;
import com.bank.bareillycitybankofindia.service.impl.CustomerServiceImpl;
import com.bank.bareillycitybankofindia.service.impl.TransferServiceImpl;
import com.bank.bareillycitybankofindia.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/customer/accounts", "/customer/account-details", "/customer/create-account", "/customer/freeze-account", "/customer/close-account"})
public class AccountServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private final CardService cardService = new CardServiceImpl();
    private final TransferService transferService = new TransferServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        String path = req.getServletPath();
        if ("/customer/account-details".equals(path)) {
            String accIdStr = req.getParameter("id");
            if (accIdStr != null) {
                long accId = Long.parseLong(accIdStr);
                Account account = accountService.getAccountById(accId);
                if (account != null) {
                    Customer owner = customerService.getCustomerById(account.getCustomerId());
                    Card card = cardService.getCardByAccountId(account.getId());
                    List<Transfer> transactions = transferService.getAccountStatement(account.getId(), 10);

                    req.setAttribute("account", account);
                    req.setAttribute("owner", owner != null ? owner : customer);
                    req.setAttribute("card", card);
                    req.setAttribute("transactions", transactions);
                }
            }
            req.getRequestDispatcher("/WEB-INF/views/customer/account-details.jsp").forward(req, resp);
        } else {
            List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
            req.setAttribute("accounts", accounts);
            req.getRequestDispatcher("/WEB-INF/views/customer/accounts.jsp").forward(req, resp);
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
            if ("/customer/create-account".equals(path)) {
                int productId = 1;
                String prodStr = req.getParameter("productId");
                if (prodStr != null && !prodStr.trim().isEmpty()) productId = Integer.parseInt(prodStr);
                String type = (productId == 4) ? "CURRENT" : "SAVINGS";

                Account account = accountService.createAccount(customer.getId(), productId, type);

                if (isAjax) {
                    JsonUtil.sendJsonResponse(resp, 200, true, "Account created successfully with ₹ 0.00 initial balance!", account);
                } else {
                    resp.sendRedirect(req.getContextPath() + "/customer/accounts");
                }
            } else if ("/customer/freeze-account".equals(path)) {
                long accId = Long.parseLong(req.getParameter("accountId"));
                accountService.freezeAccount(accId, "Requested by customer");
                JsonUtil.sendJsonResponse(resp, 200, true, "Account frozen successfully.", null);

            } else if ("/customer/close-account".equals(path)) {
                long accId = Long.parseLong(req.getParameter("accountId"));
                String reason = req.getParameter("reason");
                accountService.requestAccountClosure(accId, reason);
                JsonUtil.sendJsonResponse(resp, 200, true, "Account closure completed.", null);
            }
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, 400, false, e.getMessage(), null);
        }
    }
}
