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

@WebServlet("/customer/dashboard")
public class CustomerDashboardServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private final TransferService transferService = new TransferServiceImpl();
    private final CardService cardService = new CardServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");

        Customer customer = customerService.getCustomerByUserId(user.getId());
        if (customer == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
        List<Transfer> transactions = transferService.getCustomerTransactions(customer.getId(), 10);

        Card card = null;
        if (!accounts.isEmpty()) {
            card = cardService.getCardByAccountId(accounts.get(0).getId());
        }

        req.setAttribute("customer", customer);
        req.setAttribute("accounts", accounts);
        req.setAttribute("transactions", transactions);
        req.setAttribute("card", card);

        req.getRequestDispatcher("/WEB-INF/views/customer/dashboard.jsp").forward(req, resp);
    }
}
