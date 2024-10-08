package com.dockerwebapp.repository.impl;

import com.dockerwebapp.db.QueryExecutor;
import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.Message;
import com.dockerwebapp.model.User;
import com.dockerwebapp.repository.ChatRepository;
import com.dockerwebapp.repository.MessageRepository;
import com.dockerwebapp.repository.UserManagementRepository;
import com.dockerwebapp.repository.mapper.MessageMapperRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageRepositoryImpl implements MessageRepository {
    private final QueryExecutor queryExecutor;

    public MessageRepositoryImpl() {
        this.queryExecutor = new QueryExecutor();
    }

    private ChatRepository chatRepository =  new ChatRepositoryImpl();
    private UserManagementRepository userRepository = new UserManagementRepositoryImpl();

    @Override
    public List<Message> findAll(Long chatId) throws SQLException {
        String sql = """
               SELECT m.id, m.text, m.date_time, m.sender_id, m.chat_id 
               FROM messages m WHERE m.chat_id = ?
               """;

        return queryExecutor.executeQuery(
                sql,
                new Object[]{chatId},
                resultSet -> {
                    List<Message> messages = new ArrayList<>();
                    try {
                        while (resultSet.next()) {
                            Message message = MessageMapperRepo.mapResultSetToMessage(resultSet);

                            User sender = userRepository.getById(message.getSenderId().getId());
                            Chat chat = chatRepository.getChatById(message.getChatId().getId());
                            message.setSenderId(sender);
                            message.setChatId(chat);
                            messages.add(message);
                        }
                    } catch (SQLException e) {
                        throw new IllegalArgumentException("Error processing result set", e);
                    }
                    return messages;
                });
    }


    @Override
    public Message findById(Long id) throws SQLException {
        String sql = """
                SELECT m.id, m.text, m.date_time, m.sender_id, m.chat_id 
                FROM messages m WHERE m.id = ?
                """;

        return queryExecutor.executeQuery(
                sql,
                new Object[]{id},
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            Message message = MessageMapperRepo.mapResultSetToMessage(resultSet);
                            User sender = userRepository.getById(resultSet.getLong("sender_id"));
                            Chat chat = chatRepository.getChatById(resultSet.getLong("chat_id"));
                            message.setSenderId(sender);
                            message.setChatId(chat);
                            return message;
                        }
                    } catch (SQLException e) {
                        throw new IllegalArgumentException(e);
                    }
                    return null;
                }
        );
    }


    @Override
    public void save(Message message) throws SQLException {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        String sql = "INSERT INTO messages (text, sender_id, chat_id, date_time) VALUES (?, ?, ?, ?)";

        queryExecutor.executeUpdate(
                sql,
                new Object[]{
                        message.getText(),
                        message.getSenderId().getId(),
                        message.getChatId().getId(),
                        Timestamp.valueOf(message.getDateTime())
                }
        );
    }

    @Override
    public void update(Message message) throws SQLException {
        String sql = "UPDATE messages SET text = ?, date_time = ? WHERE id = ?";
        queryExecutor.executeUpdate(sql, new Object[]{
                message.getText(),
                Timestamp.valueOf(message.getDateTime()),
                message.getId()
        });
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM messages WHERE id = ?";
        queryExecutor.executeUpdate(sql, new Object[]{id});
    }




    @Override
    public List<Message> findByChatId(Long chatId) throws SQLException {
        String sql = """
                SELECT m.id, m.text, m.date_time, u.id AS sender_id, u.username AS sender_username, 
                m.chat_id AS chat_id, c.name AS chat_name 
                FROM messages m JOIN users u ON m.sender_id = u.id 
                JOIN chats c ON m.chat_id = c.id WHERE m.chat_id = ?
                """;

        return queryExecutor.executeQuery(
                sql,
                new Object[]{chatId},
                resultSet -> {
                    List<Message> messages = new ArrayList<>();
                    try {
                        while (resultSet.next()) {
                            messages.add(MessageMapperRepo.mapResultSetToMessage(resultSet));
                        }
                    } catch (SQLException e) {
                        throw new IllegalArgumentException("Error processing result set", e);
                    }

                    return messages;
                });
    }
}