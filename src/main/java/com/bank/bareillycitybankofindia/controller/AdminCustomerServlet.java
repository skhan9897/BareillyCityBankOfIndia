package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.model.Account;
import com.bank.bareillycitybankofindia.model.Customer;
import com.bank.bareillycitybankofindia.model.Transfer;
import com.bank.bareillycitybankofindia.model.User;
import com.bank.bareillycitybankofindia.service.AccountService;
import com.bank.bareillycitybankofindia.service.AdminService;
import com.bank.bareillycitybankofindia.service.CustomerService;
import com.bank.bareillycitybankofindia.service.TransferService;
import com.bank.bareillycitybankofindia.service.impl.AccountServiceImpl;
import com.bank.bareillycitybankofindia.service.impl.AdminServiceImpl;
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

@WebServlet(urlPatterns = {"/admin/customers", "/admin/customer-details", "/admin/approve-kyc", "/admin/reject-kyc"})
public class AdminCustomerServlet extends HttpServlet {

    private final AdminService adminService = new AdminServiceImpl();
    private final CustomerService customerService = new CustomerServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private final TransferService transferService = new TransferServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/admin/customer-details".equals(path)) {
            long custId = Long.parseLong(req.getParameter("id"));
            Customer customer = customerService.getCustomerById(custId);
            if (customer != null) {
                List<Account> accounts = accountService.getAccountsByCustomerId(customer.getId());
                List<Transfer> transactions = transferService.getCustomerTransactions(customer.getId(), 20);

                req.setAttribute("customer", customer);
                req.setAttribute("accounts", accounts);
                req.setAttribute("transactions", transactions);
            }
            req.getRequestDispatcher("/WEB-INF/views/admin/customer-details.jsp").forward(req, resp);
        } else {
            String q = req.getParameter("q");
            List<Customer> customers = (q != null && !q.trim().isEmpty()) ?
                    adminService.searchCustomers(q) : adminService.getAllCustomers(0, 100);
            req.setAttribute("customers", customers);
            req.getRequestDispatcher("/WEB-INF/views/admin/customers.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User reviewer = (User) session.getAttribute("LOGGED_IN_USER");

        String path = req.getServletPath();
        try {
            long custId = Long.parseLong(req.getParameter("customerId"));
            String remarks = req.getParameter("remarks");

            if ("/admin/approve-kyc".equals(path)) {
                adminService.approveKyc(custId, reviewer.getId(), remarks);
                JsonUtil.sendJsonResponse(resp, 200, true, "KYC approved successfully.", null);
            } else if ("/admin/reject-kyc".equals(path)) {
                adminService.rejectKyc(custId, reviewer.getId(), remarks);
                JsonUtil.sendJsonResponse(resp, 200, true, "KYC rejected.", null);
            }
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, 400, false, e.getMessage(), null);
        }
    }
}
