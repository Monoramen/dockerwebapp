package com.dockerwebapp.db;


import java.util.Properties;

public class DataBaseConnectorFactory {
    private static DataBaseConnector instance;

    private DataBaseConnectorFactory() {
    }

    public static synchronized DataBaseConnector getInstance(Properties properties) {
        if (instance == null || properties != null) {
            instance = new DataBaseConnector(properties);
        }
        return instance;
    }

    public static synchronized DataBaseConnector getInstance() {
        if (instance == null) {
            instance = new DataBaseConnector();
        }
        return instance;
    }
}