package com.bank.bareillycitybankofindia.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static HikariDataSource dataSource;

    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            initDataSource();
        }
        return dataSource;
    }

    public static synchronized Connection getConnection() throws Exception {
        return getDataSource().getConnection();
    }

    private static void initDataSource() {
        String dbUrl = AppConfig.get("db.url", "jdbc:mysql://localhost:3306/bareilly_bank?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        String dbUser = AppConfig.get("db.username", "root");
        String dbPassword = AppConfig.get("db.password", "");

        boolean mysqlAvailable = testConnection("com.mysql.cj.jdbc.Driver", dbUrl, dbUser, dbPassword);

        HikariConfig config = new HikariConfig();
        if (mysqlAvailable) {
            logger.info("Connecting to MySQL Database at {}", dbUrl);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl(dbUrl);
            config.setUsername(dbUser);
            config.setPassword(dbPassword);
        } else {
            logger.info("MySQL unavailable. Falling back to embedded H2 Database in MySQL mode.");
            config.setDriverClassName("org.h2.Driver");
            config.setJdbcUrl("jdbc:h2:mem:bareilly_bank;MODE=MySQL;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE;DB_CLOSE_DELAY=-1");
            config.setUsername("sa");
            config.setPassword("");
        }

        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setAutoCommit(true);

        dataSource = new HikariDataSource(config);
    }

    private static boolean testConnection(String driverClass, String url, String user, String password) {
        try {
            Class.forName(driverClass);
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static synchronized void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            dataSource = null;
        }
    }
}
