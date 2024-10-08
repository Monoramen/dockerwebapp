package com.dockerwebapp.db;

import java.sql.*;
import java.util.Properties;
import java.util.function.Function;

public class QueryExecutor {
    private DataBaseConnector dataBaseConnector;

    public QueryExecutor(Properties properties) {
        this.dataBaseConnector = DataBaseConnectorFactory.getInstance(properties);
    }

    public QueryExecutor() {
        this.dataBaseConnector = DataBaseConnectorFactory.getInstance();
    }

    public <T> T executeQuery(String sql, Object[] params, Function<ResultSet, T> resultMapper) throws SQLException {
        try (Connection connection = dataBaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultMapper.apply(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public int executeUpdate(String sql, Object[] params) throws SQLException {
        try (Connection connection = dataBaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, params);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void setParameters(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }
}