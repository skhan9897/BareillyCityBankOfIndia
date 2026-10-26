package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.model.LoanApplication;
import com.bank.bareillycitybankofindia.model.User;
import com.bank.bareillycitybankofindia.service.AdminService;
import com.bank.bareillycitybankofindia.service.LoanService;
import com.bank.bareillycitybankofindia.service.impl.AdminServiceImpl;
import com.bank.bareillycitybankofindia.service.impl.LoanServiceImpl;
import com.bank.bareillycitybankofindia.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/loans", "/admin/approve-loan"})
public class AdminLoanServlet extends HttpServlet {

    private final AdminService adminService = new AdminServiceImpl();
    private final LoanService loanService = new LoanServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<LoanApplication> applications = adminService.getAllLoanApplications();
        req.setAttribute("applications", applications);
        req.getRequestDispatcher("/WEB-INF/views/admin/loans.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User reviewer = (User) session.getAttribute("LOGGED_IN_USER");

        try {
            long appId = Long.parseLong(req.getParameter("applicationId"));
            loanService.approveAndDisburseLoan(appId, reviewer.getId());
            JsonUtil.sendJsonResponse(resp, 200, true, "Loan approved and funds disbursed to customer account!", null);
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, 400, false, e.getMessage(), null);
        }
    }
}
