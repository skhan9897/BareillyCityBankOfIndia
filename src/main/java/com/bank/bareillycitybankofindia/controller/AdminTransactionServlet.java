package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.model.Transfer;
import com.bank.bareillycitybankofindia.service.AdminService;
import com.bank.bareillycitybankofindia.service.impl.AdminServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/transactions", "/admin/reversals"})
public class AdminTransactionServlet extends HttpServlet {

    private final AdminService adminService = new AdminServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        List<Transfer> transactions = adminService.getAllTransactions(0, 100);

        req.setAttribute("transactions", transactions);

        if ("/admin/reversals".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/admin/reversals.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/admin/transactions.jsp").forward(req, resp);
        }
    }
}
