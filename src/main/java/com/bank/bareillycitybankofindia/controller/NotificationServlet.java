package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.dao.NotificationDao;
import com.bank.bareillycitybankofindia.dao.impl.NotificationDaoImpl;
import com.bank.bareillycitybankofindia.model.Notification;
import com.bank.bareillycitybankofindia.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/customer/notifications")
public class NotificationServlet extends HttpServlet {

    private final NotificationDao notificationDao = new NotificationDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("LOGGED_IN_USER");

        List<Notification> notifications = notificationDao.findByUserId(user.getId(), 50);
        req.setAttribute("notifications", notifications);

        req.getRequestDispatcher("/WEB-INF/views/customer/notifications.jsp").forward(req, resp);
    }
}
