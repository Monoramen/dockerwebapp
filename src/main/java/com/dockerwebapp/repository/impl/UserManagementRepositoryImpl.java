package com.dockerwebapp.repository.impl;

import com.dockerwebapp.db.QueryExecutor;
import com.dockerwebapp.model.User;
import com.dockerwebapp.repository.UserManagementRepository;
import com.dockerwebapp.repository.mapper.UserManagementMapper;
import java.sql.SQLException;


public class UserManagementRepositoryImpl implements UserManagementRepository {
    private final QueryExecutor queryExecutor;

    public UserManagementRepositoryImpl() {
        this.queryExecutor = new QueryExecutor();
    }

    @Override
    public void createUser(User user) throws SQLException {
        String sql = """
                INSERT INTO users (username, password) VALUES (?, ?) 
                ON CONFLICT (username) DO UPDATE 
                SET password = EXCLUDED.password 
                """;
        queryExecutor.executeUpdate(
                sql,
                new Object[]{
                        user.getUsername(),
                        user.getPassword()
                });
    }

    @Override
    public void updateUser(User user) throws SQLException {
        String sql = """
                UPDATE users SET username = ?, 
                first_name = ?, 
                last_name = ?, 
                about = ?, 
                password = ? 
                WHERE id = ?
                """;
        queryExecutor.executeUpdate(
                sql,
                new Object[]{
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getAbout(),
                        user.getPassword(),
                        user.getId()});

    }

    @Override
    public void deleteUser(String username) throws SQLException {
        String getUserIdSql = "SELECT id FROM users WHERE username = ?";
        Long userId = queryExecutor.executeQuery(getUserIdSql, new Object[]{username}, resultSet -> {
            try {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                }
            } catch (SQLException e) {
                new SQLException("Cannot find user");
            }
            return null;
        });

        if (userId != null) {

            String updateMessagesSql = "UPDATE messages SET sender_id = NULL WHERE sender_id = ?";
            queryExecutor.executeUpdate(updateMessagesSql, new Object[]{userId});

            String deleteParticipantsSql = "DELETE FROM chat_participants WHERE user_id = ?";
            queryExecutor.executeUpdate(deleteParticipantsSql, new Object[]{userId});

            String deleteUserSql = "DELETE FROM users WHERE id = ?";
            queryExecutor.executeUpdate(deleteUserSql, new Object[]{userId});
        }
    }

    @Override
    public User getByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        return queryExecutor.executeQuery(sql, new Object[]{username}, resultSet -> {
            try {
                if (resultSet.next()) {
                    return UserManagementMapper.mapResultSetToUserManagement(resultSet);
                }
            } catch (SQLException e) {
                new SQLException("Cannot find in the DataBase");
            }
            return null;
        });
    }

    @Override
    public User getById(Long id) throws SQLException {
        String sql = """
                SELECT u.*, (SELECT json_agg(c) FROM chats c JOIN chat_participants cp 
                ON c.id = cp.chat_id WHERE cp.user_id = u.id) AS chats 
                FROM users u WHERE u.id = ?
                """;

        return queryExecutor.executeQuery(sql, new Object[]{id}, resultSet -> {
            try {
                if (resultSet.next()) {
                    return UserManagementMapper.mapResultSetToUser(resultSet);
                }
            } catch (SQLException e) {
                new SQLException("Cannot find user");
            }
            return null;
        });
    }


}

