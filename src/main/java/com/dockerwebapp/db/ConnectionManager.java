package com.dockerwebapp.db;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionManager {
    static Connection getConnection() throws SQLException {
        return null;
    }
}
