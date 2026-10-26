package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.dao.ServiceRequestDao;
import com.bank.bareillycitybankofindia.dao.impl.ServiceRequestDaoImpl;
import com.bank.bareillycitybankofindia.model.Customer;
import com.bank.bareillycitybankofindia.model.ServiceRequest;
import com.bank.bareillycitybankofindia.model.User;
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
import java.util.List;

@WebServlet(urlPatterns = {"/customer/service-requests", "/customer/support", "/customer/create-service-request"})
public class ServiceRequestServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerServiceImpl();
    private final ServiceRequestDao serviceRequestDao = new ServiceRequestDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        String path = req.getServletPath();
        List<ServiceRequest> requests = serviceRequestDao.findByCustomerId(customer.getId());

        req.setAttribute("requests", requests);

        if ("/customer/support".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/customer/support.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/customer/service-requests.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        try {
            String requestType = req.getParameter("requestType");
            String details = req.getParameter("details");

            ServiceRequest sr = new ServiceRequest();
            sr.setRequestRef("SR" + System.currentTimeMillis());
            sr.setCustomerId(customer.getId());
            sr.setRequestType(requestType);
            sr.setDetails(details);
            sr.setStatus("PENDING");

            long id = serviceRequestDao.createRequest(sr);
            sr.setId(id);

            JsonUtil.sendJsonResponse(resp, 200, true, "Service request logged successfully! Reference: " + sr.getRequestRef(), sr);
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, 400, false, e.getMessage(), null);
        }
    }
}
