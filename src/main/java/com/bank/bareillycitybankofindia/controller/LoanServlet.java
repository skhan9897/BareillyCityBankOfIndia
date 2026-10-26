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

@WebServlet(urlPatterns = {"/customer/loans", "/customer/apply-loan", "/customer/repay-emi"})
public class LoanServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerServiceImpl();
    private final LoanService loanService = new LoanServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        List<LoanApplication> applications = loanService.getApplicationsByCustomer(customer.getId());
        List<Loan> loans = loanService.getLoansByCustomer(customer.getId());

        req.setAttribute("applications", applications);
        req.setAttribute("loans", loans);
        req.getRequestDispatcher("/WEB-INF/views/customer/loans.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        String path = req.getServletPath();

        try {
            if ("/customer/apply-loan".equals(path)) {
                int productId = Integer.parseInt(req.getParameter("productId"));
                BigDecimal amount = new BigDecimal(req.getParameter("amount"));
                int tenure = Integer.parseInt(req.getParameter("tenureMonths"));
                String purpose = req.getParameter("purpose");
                BigDecimal income = new BigDecimal(req.getParameter("monthlyIncome"));

                LoanApplication app = loanService.applyForLoan(customer.getId(), productId, amount, tenure, purpose, income);
                JsonUtil.sendJsonResponse(resp, 200, true, "Loan application submitted successfully!", app);

            } else if ("/customer/repay-emi".equals(path)) {
                long loanId = Long.parseLong(req.getParameter("loanId"));
                BigDecimal amount = new BigDecimal(req.getParameter("amount"));

                loanService.repayEmi(loanId, amount);
                JsonUtil.sendJsonResponse(resp, 200, true, "EMI payment recorded successfully!", null);
            }
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, 400, false, e.getMessage(), null);
        }
    }
}
