package com.bank.bareillycitybankofindia.filter;

import com.bank.bareillycitybankofindia.security.CsrfTokenUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CsrfFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String method = req.getMethod();
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
            String path = req.getServletPath();
            // Exclude public login/register/admin-login forms or endpoints that issue initial tokens
            if (!path.endsWith("/login") && !path.endsWith("/register") && !path.endsWith("/forgot-password") && !path.endsWith("/reset-password") && !path.endsWith("/admin-login")) {
                if (!CsrfTokenUtil.isValidToken(req)) {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"success\":false,\"message\":\"Invalid CSRF token. Request rejected for security.\"}");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
