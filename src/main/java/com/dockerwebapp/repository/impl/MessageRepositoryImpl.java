package com.dockerwebapp.repository.impl;

import com.dockerwebapp.db.QueryExecutor;
import com.dockerwebapp.model.Message;
import com.dockerwebapp.repository.MessageRepository;
import com.dockerwebapp.repository.mapper.MessageMapperRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageRepositoryImpl implements MessageRepository {
    private final QueryExecutor queryExecutor;

    public MessageRepositoryImpl() {
        this.queryExecutor = new QueryExecutor();
    }

    @Override
    public List<Message> findAll() throws SQLException {
        String sql = "SELECT " +
                "    m.id, " +
                "    m.text, " +
                "    m.date_time, " +
                "    u.id AS sender_id, " +
                "    u.username AS sender_username, " +
                "    c.id AS chat_id, " +
                "    c.name AS chat_name " +
                "FROM messages m " +
                "JOIN users u ON m.sender_id = u.id " +
                "JOIN chats c ON m.chat_id = c.id";

        return queryExecutor.executeQuery(
                sql,
                new Object[]{},
                resultSet -> {
                    List<Message> result = new ArrayList<>();
                    try {
                        while (resultSet.next()) {
                            result.add(MessageMapperRepo.mapResultSetToMessage(resultSet));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("MessageRepositoryImpl retrieved: " + result);
                    return result;
                });
    }

    @Override
    public Message findById(Long id) throws SQLException {
        String sql = "SELECT m.id, " +
                "m.text, " +
                "m.date_time, " +
                "u.id AS sender_id, " +
                "u.username AS sender_username, " +
                "c.id AS chat_id, " +
                "c.name AS chat_name " +
                "FROM messages m " +
                "JOIN users u ON m.sender_id = u.id " +
                "JOIN chats c ON m.chat_id = c.id " +
                "WHERE m.id = ?";

        return queryExecutor.executeQuery(
                sql,
                new Object[]{id},
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return MessageMapperRepo.mapResultSetToMessage(resultSet);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return null;  // Если сообщение не найдено, возвращаем null
                }
        );
    }


    @Override
    public void save(Message message) throws SQLException {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        String sql = "INSERT INTO messages (text, sender_id, chat_id, date_time) VALUES (?, ?, ?, ?)";

        // Используем executeUpdate для выполнения операции вставки
        int rowsInserted = queryExecutor.executeUpdate(
                sql,
                new Object[]{
                        message.getText(),
                        message.getSender().getId(),  // Убедитесь, что sender не равен null
                        message.getChat().getId(),     // Убедитесь, что chat не равен null
                        Timestamp.valueOf(message.getDateTime()) // Преобразуем LocalDateTime в Timestamp
                }
        );

    }

    @Override
    public void update(Message message) throws SQLException {
        String sql = "UPDATE messages SET text = ?, date_time = ? WHERE id = ?";
        queryExecutor.executeUpdate(sql, new Object[]{
                message.getText(),
                Timestamp.valueOf(message.getDateTime()), // Преобразуем LocalDateTime в Timestamp
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
        String sql = "SELECT m.id, m.text, m.date_time, " +
                "u.id AS sender_id, u.username AS sender_username, " +
                "m.chat_id AS chat_id, c.name AS chat_name " + // Добавляем chat_id и chat_name
                "FROM messages m " +
                "JOIN users u ON m.sender_id = u.id " +
                "JOIN chats c ON m.chat_id = c.id " + // Присоединяем таблицу chats
                "WHERE m.chat_id = ?";

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
                        throw new RuntimeException("Error processing result set", e);
                    }

                    return messages;
                });
    }
}