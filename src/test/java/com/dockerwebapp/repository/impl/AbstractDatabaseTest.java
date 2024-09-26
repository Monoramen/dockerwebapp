package com.dockerwebapp.repository.impl;

import com.dockerwebapp.db.DataBaseConnector;
import com.dockerwebapp.db.QueryExecutor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractDatabaseTest {
    protected DataBaseConnector dbConnector;
    protected static final String INIT_SCRIPT = "init.sql";
    protected Properties properties;
    protected QueryExecutor queryExecutor;


    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14.1-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript(INIT_SCRIPT);


    @BeforeAll
    static void setUp()  {
        postgresContainer.start();
        System.out.println("PostgreSQL container started with URL: " + postgresContainer.getJdbcUrl());
    }

    @AfterAll
    static void tearDown()  {
        postgresContainer.stop();
        System.out.println("PostgreSQL container stopped.");
    }

    protected void initializeDatabaseConnection() throws SQLException {
        properties = new Properties();
        dbConnector = new DataBaseConnector();
        properties.setProperty("db.driver", "org.postgresql.Driver");
        properties.setProperty("db.url", postgresContainer.getJdbcUrl());
        properties.setProperty("db.user", postgresContainer.getUsername());
        properties.setProperty("db.password", postgresContainer.getPassword());
        dbConnector = new DataBaseConnector(properties);

        try (Connection connection = dbConnector.getConnection()) {
            assertNotNull(connection, "Connection should not be null");
            queryExecutor = new QueryExecutor(properties);
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            throw e;
        }
    }

    protected void clearDatabase() throws SQLException {
        String sql = "DROP SCHEMA public CASCADE; CREATE SCHEMA public;";
        try (Connection connection = dbConnector.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Cleared the entire database.");
        }
    }
}

