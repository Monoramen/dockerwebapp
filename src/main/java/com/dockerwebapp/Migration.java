package com.dockerwebapp;

import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.util.Properties;

public class Migration {
    private static final String PROPERTIES_FILE = "migration.properties";
    private static Properties properties = new Properties();

    static {
        try {
            Class.forName("org.postgresql.Driver");
            properties.load(Migration.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Flyway flyway = Flyway.configure()
                .dataSource(
                        properties.getProperty("db.url"),
                        properties.getProperty("db.user"),
                        properties.getProperty("db.password"))
                .load();

        // Выполнение миграций
        flyway.migrate();
        System.out.println("Database migration completed successfully.");
    }
}