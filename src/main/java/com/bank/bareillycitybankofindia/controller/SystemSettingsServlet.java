package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.dao.SystemSettingDao;
import com.bank.bareillycitybankofindia.dao.impl.SystemSettingDaoImpl;
import com.bank.bareillycitybankofindia.model.SystemSetting;
import com.bank.bareillycitybankofindia.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/settings")
public class SystemSettingsServlet extends HttpServlet {

    private final SystemSettingDao settingDao = new SystemSettingDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<SystemSetting> settings = settingDao.findAll();
        req.setAttribute("settings", settings);
        req.getRequestDispatcher("/WEB-INF/views/admin/system-settings.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String key = req.getParameter("key");
            String value = req.getParameter("value");
            settingDao.updateValue(key, value);
            JsonUtil.sendJsonResponse(resp, 200, true, "System setting updated.", null);
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, 400, false, e.getMessage(), null);
        }
    }
}
