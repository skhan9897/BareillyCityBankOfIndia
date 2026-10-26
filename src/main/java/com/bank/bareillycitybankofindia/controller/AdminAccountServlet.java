package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.model.Account;
import com.bank.bareillycitybankofindia.model.Card;
import com.bank.bareillycitybankofindia.model.Customer;
import com.bank.bareillycitybankofindia.model.Transfer;
import com.bank.bareillycitybankofindia.model.User;
import com.bank.bareillycitybankofindia.service.AccountService;
import com.bank.bareillycitybankofindia.service.AdminService;
import com.bank.bareillycitybankofindia.service.CardService;
import com.bank.bareillycitybankofindia.service.CustomerService;
import com.bank.bareillycitybankofindia.service.TransferService;
import com.bank.bareillycitybankofindia.service.impl.AccountServiceImpl;
import com.bank.bareillycitybankofindia.service.impl.AdminServiceImpl;
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

@WebServlet(urlPatterns = {"/admin/accounts", "/admin/account-details", "/admin/account-product", "/admin/account-status"})
public class AdminAccountServlet extends HttpServlet {

    private final AdminService adminService = new AdminServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private final CustomerService customerService = new CustomerServiceImpl();
    private final CardService cardService = new CardServiceImpl();
    private final TransferService transferService = new TransferServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/admin/account-details".equals(path)) {
            long id = Long.parseLong(req.getParameter("id"));
            Account account = accountService.getAccountById(id);
            if (account != null) {
                Customer owner = customerService.getCustomerById(account.getCustomerId());
                Card card = cardService.getCardByAccountId(account.getId());
                List<Transfer> transactions = transferService.getAccountStatement(account.getId(), 10);

                req.setAttribute("account", account);
                req.setAttribute("owner", owner);
                req.setAttribute("card", card);
                req.setAttribute("transactions", transactions);
            }
            req.getRequestDispatcher("/WEB-INF/views/admin/account-details.jsp").forward(req, resp);
        } else {
            List<Account> accounts = adminService.getAllAccounts(0, 50);
            req.setAttribute("accounts", accounts);
            req.getRequestDispatcher("/WEB-INF/views/admin/accounts.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");

        String path = req.getServletPath();
        try {
            long accountId = Long.parseLong(req.getParameter("accountId"));
            if ("/admin/account-product".equals(path)) {
                int newProductId = Integer.parseInt(req.getParameter("productId"));
                String reason = req.getParameter("reason");
                accountService.changeAccountProduct(accountId, newProductId, reason, user.getId());
                JsonUtil.sendJsonResponse(resp, 200, true, "Account product type updated successfully.", null);

            } else if ("/admin/account-status".equals(path)) {
                String newStatus = req.getParameter("status"); // ACTIVE, FROZEN, DORMANT, CLOSED
                String reason = req.getParameter("reason");
                accountService.updateAccountStatusWithAuthorization(accountId, newStatus, reason, user.getId());
                JsonUtil.sendJsonResponse(resp, 200, true, "Account authorization status updated to " + newStatus + ".", null);
            }
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, 400, false, e.getMessage(), null);
        }
    }
}
