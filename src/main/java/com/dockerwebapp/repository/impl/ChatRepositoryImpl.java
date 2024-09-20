package com.dockerwebapp.repository.impl;

import com.dockerwebapp.db.QueryExecutor;
import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.User;
import com.dockerwebapp.repository.ChatRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatRepositoryImpl implements ChatRepository {
    private final QueryExecutor queryExecutor;

    public ChatRepositoryImpl() {
        this.queryExecutor = new QueryExecutor();
    }

    @Override
    public void addChat(Chat chat) throws SQLException {
        // Сначала добавляем чат
        String sql = "INSERT INTO chats (id, name) " +
                "VALUES (?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name";

        try {
            queryExecutor.executeUpdate(sql, new Object[]{
                    chat.getId(),
                    chat.getName()});
        } catch (SQLException e) {
            System.err.println("Error adding/updating chat: " + e.getMessage());
            throw e;
        }

        // Затем добавляем участников
        if (chat.getParticipants() != null && chat.getParticipants().size() >= 2) {
            String participantSql = "INSERT INTO chat_participants (chat_id, user_id) VALUES (?, ?)";

            for (User participant : chat.getParticipants()) {
                try {
                    queryExecutor.executeUpdate(participantSql, new Object[]{
                            chat.getId(),
                            participant.getId()
                    });
                } catch (SQLException e) {
                    System.err.println("Error adding participant: " + e.getMessage());
                    throw e;
                }
            }
        } else {
            throw new SQLException("A chat must have at least two participants.");
        }
    }
    @Override
    public void deleteChat(Chat chat) throws SQLException {
        String deleteParticipantsSql = "DELETE FROM chat_participants WHERE chat_id = ?";
        queryExecutor.executeUpdate(deleteParticipantsSql, new Object[]{chat.getId()});

        String deleteChatSql = "DELETE FROM chats WHERE id = ?";
        queryExecutor.executeUpdate(deleteChatSql, new Object[]{chat.getId()});
    }

    @Override
    public List<Chat> getUserChats(Long userId) throws SQLException {
        String sql = "SELECT c.id, c.name FROM chats c " +
                "JOIN chat_participants cp ON c.id = cp.chat_id " +
                "WHERE cp.user_id = ?";
        return queryExecutor.executeQuery(sql, new Object[]{userId}, resultSet -> {
            List<Chat> chats = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    Long chatId = resultSet.getLong("id");
                    String chatName = resultSet.getString("name");
                    chats.add(new Chat(chatId, chatName));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return chats;
        });
    }

    @Override
    public void updateChat(Chat chat) throws SQLException {
        String sql = "UPDATE chats SET name = ? WHERE id = ?";
        queryExecutor.executeUpdate(sql, new Object[]{chat.getName(), chat.getId()});
    }

}