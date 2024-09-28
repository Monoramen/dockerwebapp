package com.dockerwebapp.repository.mapper;

import com.dockerwebapp.model.Chat;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ChatMapperRepo {
    public static Chat mapResultSetToChat(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");

        if (resultSet.wasNull()) {
            return null;
        }
        Chat chat = new Chat.ChatBuilder()
                .setId(id)
                .setName(name)
                .build();

        return chat;
    }
}