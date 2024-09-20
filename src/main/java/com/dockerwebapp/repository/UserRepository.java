package com.dockerwebapp.repository;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.Message;
import com.dockerwebapp.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    List<Chat> getUserChats(Long userId) throws SQLException;


}
