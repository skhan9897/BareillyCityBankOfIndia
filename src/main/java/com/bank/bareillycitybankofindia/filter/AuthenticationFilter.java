package com.bank.bareillycitybankofindia.filter;

import com.bank.bareillycitybankofindia.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/customer/*", "/admin/*"})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("LOGGED_IN_USER") : null;

        if (user == null) {
            String requestURI = req.getRequestURI();
            if (requestURI.endsWith(".json") || "XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                res.getWriter().write("{\"success\":false,\"message\":\"Session expired or unauthorized. Please login.\"}");
            } else {
                res.sendRedirect(req.getContextPath() + "/login?error=SessionExpired");
            }
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
