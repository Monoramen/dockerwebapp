package com.dockerwebapp.repository;

import com.dockerwebapp.model.Chat;

import java.sql.SQLException;
import java.util.List;

public interface ChatRepository {

    void addChat(Chat chat) throws SQLException;

    void deleteChat(Chat chat) throws SQLException;

    List<Chat> getUserChats(Long userId) throws SQLException;

    void updateChat(Chat chat) throws SQLException;

}
