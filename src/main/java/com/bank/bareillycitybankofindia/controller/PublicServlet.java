package com.bank.bareillycitybankofindia.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"", "/index", "/about", "/services", "/contact", "/faqs"})
public class PublicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if (path == null || path.isEmpty() || "/".equals(path) || "/index".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/public/index.jsp").forward(req, resp);
        } else if ("/about".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/public/about.jsp").forward(req, resp);
        } else if ("/services".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/public/services.jsp").forward(req, resp);
        } else if ("/contact".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/public/contact.jsp").forward(req, resp);
        } else if ("/faqs".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/public/faqs.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/public/index.jsp").forward(req, resp);
        }
    }
}
