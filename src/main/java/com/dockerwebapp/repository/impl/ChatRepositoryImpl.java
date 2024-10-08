package com.dockerwebapp.repository.impl;

import com.dockerwebapp.db.QueryExecutor;
import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.Message;
import com.dockerwebapp.model.User;
import com.dockerwebapp.repository.ChatRepository;
import com.dockerwebapp.repository.mapper.ChatMapperRepo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ChatRepositoryImpl implements ChatRepository {
    private final QueryExecutor queryExecutor;

    public ChatRepositoryImpl() {
        this.queryExecutor = new QueryExecutor();
    }

    @Override
    public void addChat(Chat chat) throws SQLException {
        String sql = "INSERT INTO chats (id, name) VALUES (?, ?) ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name";

        try {
            queryExecutor.executeUpdate(sql, new Object[]{
                    chat.getId(),
                    chat.getName()});
        } catch (SQLException e) {
            throw new SQLException("Error adding chat", e);
        }

        if (chat.getParticipantIds() != null && chat.getParticipantIds().size() >= 2) {
            String participantSql = "INSERT INTO chat_participants (chat_id, user_id) VALUES (?, ?)";

            for (User participant : chat.getParticipantIds()) {
                if (!isUserExists(participant.getId())) {
                    throw new SQLException("User with ID " + participant.getId() + " does not exist.");
                }
                try {
                    queryExecutor.executeUpdate(participantSql, new Object[]{
                            chat.getId(),
                            participant.getId()
                    });
                } catch (SQLException e) {
                    throw new SQLException("Error adding participant");
                }
            }
        } else {
            throw new SQLException("A chat must have at least two participants.");
        }
    }


    private boolean isUserExists(Long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";

        return queryExecutor.executeQuery(sql, new Object[]{userId}, resultSet -> {
            try {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
            return false;
        });
    }

    @Override
    public void deleteChat(Long chatId) throws SQLException {
        String checkChatSql = "SELECT COUNT(*) FROM chats WHERE id = ?";
        int count = queryExecutor.executeQuery(checkChatSql, new Object[]{chatId}, resultSet -> {
            try {
                resultSet.next();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
            try {
                return resultSet.getInt(1);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        });

        if (count == 0) {
            throw new SQLException("Chat with ID " + chatId + " does not exist.");
        }

        String deleteParticipantsSql = "DELETE FROM chat_participants WHERE chat_id = ?";
        queryExecutor.executeUpdate(deleteParticipantsSql, new Object[]{chatId});

        String deleteChatSql = "DELETE FROM chats WHERE id = ?";
        queryExecutor.executeUpdate(deleteChatSql, new Object[]{chatId});
    }

    @Override
    public List<Chat> getUserChats(Long userId) throws SQLException {
        String sql = """
                SELECT c.id AS id, c.name AS name, cp.user_id AS participantId 
                FROM chats c 
                JOIN chat_participants cp ON c.id = cp.chat_id 
                WHERE cp.user_id = ?
                """;

        return queryExecutor.executeQuery(sql,
                new Object[]{userId},
                resultSet -> {
                    List<Chat> chats = new ArrayList<>();

                    try {
                        while (resultSet.next()) {
                            Chat chat = ChatMapperRepo.mapResultSetToChat(resultSet);
                            if (chat != null) {
                                chats.add(chat); // Добавляем чат в список
                            }
                        }
                    } catch (SQLException e) {
                        throw new IllegalArgumentException(e);
                    }
                    return chats;
                });
    }

    @Override
    public void updateChat(Chat chat) throws SQLException {
        String sql = "UPDATE chats SET name = ? WHERE id = ?";
        queryExecutor.executeUpdate(sql, new Object[]{chat.getName(), chat.getId()});
    }

    @Override
    public Chat getChatById(Long chatId) throws SQLException {
        String sql = """
                SELECT c.id AS chat_id, c.name AS chat_name, m.id AS message_id, m.text AS message_text, 
                m.sender_id AS sender_id, m.date_time AS date_time
                FROM chats c LEFT JOIN messages m ON c.id = m.chat_id WHERE c.id = ?
               """;

        return queryExecutor.executeQuery(sql, new Object[]{chatId}, resultSet -> {
            try {
                if (resultSet.next()) { // Проверяем, есть ли результат
                    Long id = resultSet.getLong("chat_id");
                    String name = resultSet.getString("chat_name");

                    Chat chat = new Chat(id, name);
                    List<Message> messages = new ArrayList<>();

                    do {
                        Long messageId = resultSet.getLong("message_id");
                        String messageText = resultSet.getString("message_text");
                        Long senderId = resultSet.getLong("sender_id");
                        LocalDateTime dateTime = resultSet.getTimestamp("date_time").toLocalDateTime();

                        if (messageId != null) {
                            User sender = new User();
                            sender.setId(senderId);
                            Message message = new Message(messageId, messageText, dateTime, sender, chat);
                            messages.add(message);
                        }
                    } while (resultSet.next());

                    chat.setMessages(messages);
                    return chat;
                }
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
            return null;
        });
    }


}