package com.dockerwebapp.repository;

import com.dockerwebapp.model.User;

import java.sql.SQLException;

public interface UserManagementRepository {
    void createUser(User user) throws SQLException;

    void updateUser(User user) throws SQLException;

    void deleteUser(String username) throws SQLException;

    User getByUsername(String username) throws SQLException;

    User getById(Long id) throws SQLException;


}
