package com.dockerwebapp.repository;

import com.dockerwebapp.db.DataBaseConnector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Testcontainers
class DataBaseConnectorTest {
    Properties testProperties = new Properties();

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void setUpBeforeClass() {
        postgresContainer.start();
    }

    @AfterAll
    static void tearDownAfterClass() {
        postgresContainer.stop();
    }


    @Test
    void testGetConnection() throws IOException {
        Properties properties = new Properties();

        properties.setProperty("db.url", postgresContainer.getJdbcUrl());
        properties.setProperty("db.user", postgresContainer.getUsername());
        properties.setProperty("db.password", postgresContainer.getPassword());
        DataBaseConnector dataBaseConnector = new DataBaseConnector(properties);
        // Получаем соединение с базой данных
        try ( Connection connection = dataBaseConnector.getConnection()){
            assertNotNull(connection);
            // Дополнительные проверки соединения можно добавить здесь
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to connect to the database");
        }
    }


    @Test
    void testGetConnectionLocal() {
        try {
            testProperties.load(getClass().getClassLoader().getResourceAsStream("db-test.properties"));
            DataBaseConnector dataBaseConnector = new DataBaseConnector(testProperties);
            Connection connection = dataBaseConnector.getConnection();
            System.out.println(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
