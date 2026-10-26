package com.bank.bareillycitybankofindia.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==================================================");
        System.out.println("Starting BAREILLYCITYBANKOFINDIA Web Application...");
        System.out.println("==================================================");
        DatabaseInitializer.initialize();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseConfig.closePool();
    }
}
