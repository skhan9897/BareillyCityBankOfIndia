package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.model.Customer;
import com.bank.bareillycitybankofindia.model.KycRecord;
import com.bank.bareillycitybankofindia.model.User;
import com.bank.bareillycitybankofindia.security.InputValidator;
import com.bank.bareillycitybankofindia.service.CustomerService;
import com.bank.bareillycitybankofindia.service.impl.CustomerServiceImpl;
import com.bank.bareillycitybankofindia.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/customer/kyc")
public class KycServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        KycRecord kyc = customerService.getKycRecord(customer.getId());

        req.setAttribute("customer", customer);
        req.setAttribute("kycRecord", kyc);
        req.getRequestDispatcher("/WEB-INF/views/customer/kyc.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        try {
            String docType = req.getParameter("documentType");
            String docNumber = req.getParameter("documentNumber");

            String masked = "AADHAAR".equalsIgnoreCase(docType) ? InputValidator.maskAadhaar(docNumber) : InputValidator.maskPan(docNumber);
            customerService.submitKyc(customer.getId(), docType, masked);

            JsonUtil.sendJsonResponse(resp, 200, true, "KYC details submitted for verification successfully!", null);
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, 400, false, e.getMessage(), null);
        }
    }
}
