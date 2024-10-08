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
        Long chatId = resultSet.getLong("chat_id");

        User sender = new User();
        sender.setId(senderId);

        Chat chat = new Chat();
        chat.setId(chatId);

        return new Message(id, text, dateTime, sender, chat);
    }
}
