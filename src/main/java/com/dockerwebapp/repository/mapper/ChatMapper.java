package com.dockerwebapp.repository.mapper;

import com.dockerwebapp.model.Chat;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatMapper {

    public static Chat mapResultSetToChat(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");

        if (resultSet.wasNull()) { // Проверка на null
            return null;
        }

        return new Chat.ChatBuilder()
                .setId(id)
                .setName(name)
                .build();
    }
}