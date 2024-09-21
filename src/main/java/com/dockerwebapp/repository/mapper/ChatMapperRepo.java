package com.dockerwebapp.repository.mapper;

import com.dockerwebapp.model.Chat;

import java.sql.ResultSet;
import java.sql.SQLException;




public class ChatMapper {

    public static Chat mapResultSetToChat(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");

        // Проверка на null для id
        if (resultSet.wasNull()) {
            return null; // Если id равен null, возвращаем null
        }

        // Создаем новый объект Chat с использованием Builder
        return new Chat.ChatBuilder()
                .setId(id)
                .setName(name)
                .build(); // Возвращаем готовый объект Chat
    }
}