package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.model.User;
import com.bank.bareillycitybankofindia.security.CsrfTokenUtil;
import com.bank.bareillycitybankofindia.service.AuthService;
import com.bank.bareillycitybankofindia.service.impl.AuthServiceImpl;
import com.bank.bareillycitybankofindia.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/login", "/logout", "/register", "/forgot-password", "/reset-password", "/admin-login"})
public class AuthenticationServlet extends HttpServlet {

    private final AuthService authService = new AuthServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User loggedInUser = (session != null) ? (User) session.getAttribute("LOGGED_IN_USER") : null;

        String path = req.getServletPath();
        if ("/logout".equals(path)) {
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login?message=LoggedOut");
            return;
        }

        // Auto-redirect only for login forms if already authenticated
        if (loggedInUser != null && ("/login".equals(path) || "/admin-login".equals(path))) {
            if ("EMPLOYEE".equalsIgnoreCase(loggedInUser.getUserType())) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/customer/dashboard");
            }
            return;
        }

        // Generate CSRF token for the forms
        req.setAttribute("csrfToken", CsrfTokenUtil.getToken(req.getSession(true)));

        if ("/login".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/public/login.jsp").forward(req, resp);
        } else if ("/admin-login".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/admin/admin-login.jsp").forward(req, resp);
        } else if ("/register".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/public/register.jsp").forward(req, resp);
        } else if ("/forgot-password".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/public/forgot-password.jsp").forward(req, resp);
        } else if ("/reset-password".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/public/reset-password.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        boolean isAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With")) || path.endsWith(".json");

        try {
            if ("/login".equals(path)) {
                String u = req.getParameter("username");
                String p = req.getParameter("password");
                User user = authService.authenticate(u, p);

                if ("EMPLOYEE".equalsIgnoreCase(user.getUserType())) {
                    throw new IllegalArgumentException("Access Denied: Bank Employees & Admins must log in through the dedicated Staff Portal at /admin-login.");
                }

                // Prevent session fixation
                HttpSession oldSession = req.getSession(false);
                if (oldSession != null) oldSession.invalidate();
                HttpSession newSession = req.getSession(true);
                newSession.setAttribute("LOGGED_IN_USER", user);
                CsrfTokenUtil.getToken(newSession);

                String redirectUrl = req.getContextPath() + "/customer/dashboard";

                if (isAjax) {
                    JsonUtil.sendJsonResponse(resp, 200, true, "Customer login successful!", redirectUrl);
                } else {
                    resp.sendRedirect(redirectUrl);
                }

            } else if ("/admin-login".equals(path)) {
                String u = req.getParameter("username");
                String p = req.getParameter("password");
                User user = authService.authenticate(u, p);

                if (!"EMPLOYEE".equalsIgnoreCase(user.getUserType())) {
                    throw new IllegalArgumentException("Access Denied: Customer credentials cannot be used on the Bank Staff Portal.");
                }

                HttpSession oldSession = req.getSession(false);
                if (oldSession != null) oldSession.invalidate();
                HttpSession newSession = req.getSession(true);
                newSession.setAttribute("LOGGED_IN_USER", user);
                CsrfTokenUtil.getToken(newSession);

                String redirectUrl = req.getContextPath() + "/admin/dashboard";
                if (isAjax) {
                    JsonUtil.sendJsonResponse(resp, 200, true, "Staff authentication successful!", redirectUrl);
                } else {
                    resp.sendRedirect(redirectUrl);
                }

            } else if ("/register".equals(path)) {
                HttpSession currentSession = req.getSession(false);
                User currentLoggedIn = (currentSession != null) ? (User) currentSession.getAttribute("LOGGED_IN_USER") : null;
                boolean isCreatedByAdmin = (currentLoggedIn != null && "EMPLOYEE".equalsIgnoreCase(currentLoggedIn.getUserType()));

                String u = req.getParameter("username");
                String e = req.getParameter("email");
                String m = req.getParameter("mobile");
                String p = req.getParameter("password");
                String fn = req.getParameter("firstName");
                String ln = req.getParameter("lastName");
                String dob = req.getParameter("dob");
                String gender = req.getParameter("gender");
                String aadhaar = req.getParameter("aadhaar");
                String pan = req.getParameter("pan");
                String addr = req.getParameter("address");

                int branchId = 1;
                String branchStr = req.getParameter("branchId");
                if (branchStr != null && !branchStr.trim().isEmpty()) branchId = Integer.parseInt(branchStr);

                int productId = 1;
                String prodStr = req.getParameter("productId");
                if (prodStr != null && !prodStr.trim().isEmpty()) productId = Integer.parseInt(prodStr);

                User newUser = authService.registerCustomer(u, e, m, p, fn, ln, dob, gender, aadhaar, pan, addr, branchId, productId);

                if (isCreatedByAdmin) {
                    // Admin remains logged in as admin and redirects to customer directory
                    String redirectUrl = req.getContextPath() + "/admin/customers";
                    if (isAjax) {
                        JsonUtil.sendJsonResponse(resp, 200, true, "New Customer Account Created Successfully!", redirectUrl);
                    } else {
                        resp.sendRedirect(redirectUrl);
                    }
                } else {
                    // Customer self-registration: set new user in session
                    HttpSession newSession = req.getSession(true);
                    newSession.setAttribute("LOGGED_IN_USER", newUser);

                    String redirectUrl = req.getContextPath() + "/customer/dashboard";
                    if (isAjax) {
                        JsonUtil.sendJsonResponse(resp, 200, true, "Registration successful!", redirectUrl);
                    } else {
                        resp.sendRedirect(redirectUrl);
                    }
                }

            } else if ("/forgot-password".equals(path)) {
                String e = req.getParameter("email");
                String otp = authService.generatePasswordResetOtp(e);
                req.setAttribute("email", e);
                req.setAttribute("otpDemo", otp); // Shown for easy sandbox testing
                req.setAttribute("successMessage", "OTP sent to your email. (Demo OTP: " + otp + ")");
                req.getRequestDispatcher("/WEB-INF/views/public/reset-password.jsp").forward(req, resp);

            } else if ("/reset-password".equals(path)) {
                String e = req.getParameter("email");
                String otp = req.getParameter("otp");
                String newP = req.getParameter("newPassword");
                authService.resetPasswordWithOtp(e, otp, newP);

                if (isAjax) {
                    JsonUtil.sendJsonResponse(resp, 200, true, "Password reset successfully! Please login.", req.getContextPath() + "/login");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/login?message=PasswordResetSuccess");
                }
            }
        } catch (Exception ex) {
            if (isAjax) {
                JsonUtil.sendJsonResponse(resp, 400, false, ex.getMessage(), null);
            } else {
                req.setAttribute("errorMessage", ex.getMessage());
                doGet(req, resp);
            }
        }
    }
}
