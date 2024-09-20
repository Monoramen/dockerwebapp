package com.dockerwebapp.service;

import com.dockerwebapp.model.User;
import com.dockerwebapp.servlet.dto.UserDto;

import java.sql.SQLException;

public interface UserManagementService {
    void createUser(UserDto userDto) throws SQLException;

    void updateUser(UserDto userDto) throws SQLException;

    void deleteUser(String username) throws SQLException;

    User getByUsername(String username) throws SQLException;

    User getById(Long id) throws SQLException;
}
