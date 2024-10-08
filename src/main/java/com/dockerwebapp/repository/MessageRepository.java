package com.dockerwebapp.repository;

import com.dockerwebapp.model.Message;

import java.sql.SQLException;
import java.util.List;

public interface MessageRepository {

    List<Message> findAll(Long chatId) throws SQLException;

    Message findById(Long id) throws SQLException;

    void save(Message message) throws SQLException;

    void update(Message message) throws SQLException;

    void delete(Long id) throws SQLException;

    List<Message> findByChatId(Long chatId) throws SQLException;

}
