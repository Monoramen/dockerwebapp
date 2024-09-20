package com.dockerwebapp.repository.impl;



import com.dockerwebapp.db.QueryExecutor;
import com.dockerwebapp.model.Chat;
import com.dockerwebapp.repository.UserRepository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final QueryExecutor queryExecutor;

    public UserRepositoryImpl() {
        this.queryExecutor = new QueryExecutor();
    }

    @Override
    public List<Chat> getUserChats(Long userId) throws SQLException {
        String sql = "SELECT c.id, c.name " +
                "FROM chats c " +
                "JOIN chat_participants cp ON c.id = cp.chat_id " +
                "WHERE cp.user_id = ?";

        return queryExecutor.executeQuery(
                sql,
                new Object[]{userId},
                resultSet -> {
                    List<Chat> chats = new ArrayList<>();
                    try {
                        while (resultSet.next()) {
                            Long chatId = resultSet.getLong("id");
                            String chatName = resultSet.getString("name");
                            chats.add(new Chat(chatId, chatName));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return chats;
                }
        );
    }



}
