package com.dockerwebapp.repository.mapper;

import com.dockerwebapp.model.Chat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatMapperRepo {
    public static Chat mapResultSetToChat(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("chat_id");
        String name = resultSet.getString("chat_name");

        // Проверка на null для id
        if (resultSet.wasNull()) {
            return null; // Если id равен null, возвращаем null
        }

        // Создаем новый объект Chat с использованием Builder
        Chat chat = new Chat.ChatBuilder()
                .setId(id)
                .setName(name)
                .build(); // Возвращаем готовый объект Chat

        // Извлекаем идентификаторы участников
        List<Long> participantIds = new ArrayList<>();
        do {
            Long participantId = resultSet.getLong("user_id"); // Предполагается, что user_id извлекается из запроса
            if (!resultSet.wasNull()) {
                participantIds.add(participantId);
            }
        } while (resultSet.next());

        chat.setParticipantIds(participantIds); // Устанавливаем идентификаторы участников в чат

        return chat;
    }
}