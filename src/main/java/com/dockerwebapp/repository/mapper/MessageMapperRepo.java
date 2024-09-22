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
        String chatName = resultSet.getString("chat_name");
        return new Message(id, text, senderId, chatId);
    }
}
