package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.model.User;
import com.bank.bareillycitybankofindia.security.CsrfTokenUtil;
import com.bank.bareillycitybankofindia.service.AdminService;
import com.bank.bareillycitybankofindia.service.impl.AdminServiceImpl;
import com.bank.bareillycitybankofindia.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet(urlPatterns = {"/admin/dashboard", "/admin/deposit", "/admin/withdraw", "/admin/transfer", "/admin/freeze-account", "/admin/unfreeze-account", "/admin/block-account", "/admin/suspend-account"})
public class AdminDashboardServlet extends HttpServlet {

    private final AdminService adminService = new AdminServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        req.setAttribute("csrfToken", CsrfTokenUtil.getToken(session));

        Map<String, Object> stats = adminService.getDashboardStats();
        req.setAttribute("stats", stats);
        req.setAttribute("allAccounts", adminService.getAllAccounts(0, 50));
        req.setAttribute("allCustomers", adminService.getAllCustomers(0, 50));
        req.setAttribute("recentTransactions", adminService.getAllTransactions(0, 10));
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");

        String path = req.getServletPath();
        try {
            if ("/admin/deposit".equals(path)) {
                String accNum = req.getParameter("accountNumber");
                BigDecimal amount = new BigDecimal(req.getParameter("amount"));
                String desc = req.getParameter("description");
                adminService.depositCash(accNum, amount, desc, user.getId());
                JsonUtil.sendJsonResponse(resp, 200, true, "Cash of ₹ " + amount + " deposited successfully into " + accNum, null);

            } else if ("/admin/withdraw".equals(path)) {
                String accNum = req.getParameter("accountNumber");
                BigDecimal amount = new BigDecimal(req.getParameter("amount"));
                String desc = req.getParameter("description");
                adminService.withdrawCash(accNum, amount, desc, user.getId());
                JsonUtil.sendJsonResponse(resp, 200, true, "Cash of ₹ " + amount + " withdrawn successfully from " + accNum, null);

            } else if ("/admin/transfer".equals(path)) {
                String fromAcc = req.getParameter("fromAccount");
                String toAcc = req.getParameter("toAccount");
                BigDecimal amount = new BigDecimal(req.getParameter("amount"));
                String desc = req.getParameter("description");
                adminService.adminTransfer(fromAcc, toAcc, amount, desc, user.getId());
                JsonUtil.sendJsonResponse(resp, 200, true, "Admin transfer of ₹ " + amount + " completed successfully!", null);

            } else if ("/admin/block-account".equals(path) || "/admin/freeze-account".equals(path)) {
                long accId = Long.parseLong(req.getParameter("accountId"));
                String reason = req.getParameter("reason");
                adminService.toggleAccountStatus(accId, "BLOCKED", reason != null ? reason : "Admin Block", user.getId());
                JsonUtil.sendJsonResponse(resp, 200, true, "Account status updated to BLOCKED / FROZEN.", null);

            } else if ("/admin/suspend-account".equals(path)) {
                long accId = Long.parseLong(req.getParameter("accountId"));
                String reason = req.getParameter("reason");
                adminService.toggleAccountStatus(accId, "SUSPENDED", reason != null ? reason : "Admin Suspended", user.getId());
                JsonUtil.sendJsonResponse(resp, 200, true, "Account status updated to SUSPENDED.", null);

            } else if ("/admin/unfreeze-account".equals(path)) {
                long accId = Long.parseLong(req.getParameter("accountId"));
                String reason = req.getParameter("reason");
                adminService.toggleAccountStatus(accId, "ACTIVE", reason != null ? reason : "Admin Unblocked", user.getId());
                JsonUtil.sendJsonResponse(resp, 200, true, "Account status updated to ACTIVE (Unblocked).", null);
            }
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, 400, false, e.getMessage(), null);
        }
    }
}
