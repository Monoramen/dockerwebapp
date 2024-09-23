package com.dockerwebapp.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class DataBaseConnector implements ConnectionManager {
    private static final String PROPERTIES_FILE = "db.properties";
    private Properties properties;

    private String db_url;
    private String db_user;
    private String db_password;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.getMessage();
            throw new RuntimeException("PostgreSQL driver not found", e);
        }
    }

    public DataBaseConnector(Properties properties) {
        this.properties = properties;
        this.db_url = properties.getProperty("db.url");
        this.db_user = properties.getProperty("db.user");
        this.db_password = properties.getProperty("db.password");
    }

    public DataBaseConnector() {
        this.properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE));
            this.db_url = properties.getProperty("db.url");
            this.db_user = properties.getProperty("db.user");
            this.db_password = properties.getProperty("db.password");
        } catch (IOException e) {
            e.getMessage();
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(db_url, db_user, db_password);
            return conn;
        } catch (SQLException e) {
            e.getMessage()      ;
            throw e;
        }
    }
}