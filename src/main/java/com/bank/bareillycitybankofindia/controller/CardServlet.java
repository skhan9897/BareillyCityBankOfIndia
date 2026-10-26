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
import java.util.List;

@WebServlet(urlPatterns = {"/customer/cards", "/customer/upi", "/customer/block-card", "/customer/unblock-card", "/customer/card-limit"})
public class CardServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private final CardService cardService = new CardServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        String path = req.getServletPath();
        List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());

        Card card = null;
        if (!accounts.isEmpty()) {
            card = cardService.getCardByAccountId(accounts.get(0).getId());
        }

        req.setAttribute("accounts", accounts);
        req.setAttribute("card", card);

        if ("/customer/upi".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/customer/upi.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/customer/cards.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        try {
            if ("/customer/block-card".equals(path)) {
                long cardId = Long.parseLong(req.getParameter("cardId"));
                cardService.blockCard(cardId);
                JsonUtil.sendJsonResponse(resp, 200, true, "Card blocked successfully.", null);

            } else if ("/customer/unblock-card".equals(path)) {
                long cardId = Long.parseLong(req.getParameter("cardId"));
                cardService.unblockCard(cardId);
                JsonUtil.sendJsonResponse(resp, 200, true, "Card unblocked successfully.", null);

            } else if ("/customer/card-limit".equals(path)) {
                long cardId = Long.parseLong(req.getParameter("cardId"));
                double limit = Double.parseDouble(req.getParameter("dailyLimit"));
                cardService.updateDailyLimit(cardId, limit);
                JsonUtil.sendJsonResponse(resp, 200, true, "Card daily transaction limit updated.", null);
            }
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, 400, false, e.getMessage(), null);
        }
    }
}
