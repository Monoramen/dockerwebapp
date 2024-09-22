package com.dockerwebapp.service;

import com.dockerwebapp.model.User;
import com.dockerwebapp.servlet.dto.UserDto;
import com.dockerwebapp.servlet.dto.CreateUserDto;

import java.sql.SQLException;

public interface UserManagementService {

    void createUser(CreateUserDto CreateUserDto);

    void updateUser(UserDto userDto) throws SQLException;

    void deleteUser(String username) throws SQLException;

    UserDto getByUsername(String username) throws SQLException;

    UserDto getById(Long id) throws SQLException;

}
