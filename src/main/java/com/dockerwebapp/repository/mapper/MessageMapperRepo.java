package com.dockerwebapp.repository.mapper;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.Message;
import com.dockerwebapp.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;


public class MessageMapperRepo {
    public static Message mapResultSetToMessage(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String text = resultSet.getString("text");
        LocalDateTime dateTime = resultSet.getTimestamp("date_time").toLocalDateTime();

        Long senderId = resultSet.getLong("sender_id");
        String senderUsername = resultSet.getString("sender_username");
        User sender = new User.UserBuilder(senderId, senderUsername, "default_password").build();

        Long chatId = resultSet.getLong("chat_id");
        String chatName = resultSet.getString("chat_name"); // Убедитесь, что это поле доступно
        Chat chat = new Chat(chatId, chatName);

        return new Message(id, text, sender, chat);
    }
}
