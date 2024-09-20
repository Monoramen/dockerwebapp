package com.dockerwebapp.service.impl;

import com.dockerwebapp.model.User;
import com.dockerwebapp.repository.UserManagementRepository;
import com.dockerwebapp.repository.UserRepository;
import com.dockerwebapp.repository.impl.UserManagementRepositoryImpl;

import com.dockerwebapp.service.UserManagementService;
import com.dockerwebapp.servlet.dto.UserDto;
import com.dockerwebapp.servlet.mapper.UserManagementMapper;

import java.sql.SQLException;

public class UserManagementServiceImpl implements UserManagementService {

    private UserManagementRepository userRepository;
    private final UserManagementMapper userManagementMapper = UserManagementMapper.INSTANCE;

    public UserManagementServiceImpl() {
        this.userRepository = new UserManagementRepositoryImpl();
    }

    @Override
    public void createUser(UserDto userDto) {
        User user = userManagementMapper.convert(userDto);
        try {
            userRepository.createUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateUser(UserDto userDto) throws SQLException {
        User user = userManagementMapper.convert(userDto);
        try {
            userRepository.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteUser(String username) throws SQLException {
        try {
            userRepository.deleteUser(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getByUsername(String username) throws SQLException {
        return null;
    }

    @Override
    public User getById(Long id) throws SQLException {
        User user = userManagementMapper.convert(userRepository.getById(id));
        return user;
    }
}
