package com.dockerwebapp.repository;

import com.dockerwebapp.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserManagementRepository {
    void createUser(User user) throws SQLException;

    void updateUser(User user) throws SQLException;

    void deleteUser(String username) throws SQLException;

    User getByUsername(String username) throws SQLException;

    User getById(Long id) throws SQLException;

    List<User> findAll() throws SQLException;
}
