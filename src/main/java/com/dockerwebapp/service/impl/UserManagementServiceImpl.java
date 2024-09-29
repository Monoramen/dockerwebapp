package com.dockerwebapp.service.impl;

import com.dockerwebapp.model.User;
import com.dockerwebapp.repository.UserManagementRepository;
import com.dockerwebapp.repository.impl.UserManagementRepositoryImpl;

import com.dockerwebapp.service.UserManagementService;
import com.dockerwebapp.servlet.dto.UserDto;
import com.dockerwebapp.servlet.dto.CreateUserDto;
import com.dockerwebapp.servlet.mapper.CreateUserMapper;
import com.dockerwebapp.servlet.mapper.UserManagementMapper;

import java.sql.SQLException;

public class UserManagementServiceImpl implements UserManagementService {

    private UserManagementRepository userRepository;
    private final UserManagementMapper userManagementMapper = UserManagementMapper.INSTANCE;
    private final CreateUserMapper createUserMapper = CreateUserMapper.INSTANCE;


    public UserManagementServiceImpl() {
        this.userRepository = new UserManagementRepositoryImpl();
    }

    public UserManagementServiceImpl(UserManagementRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(CreateUserDto createUserDto) {
        User user = createUserMapper.convert(createUserDto);
        try {
            userRepository.createUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Database error while creating user: " + e.getMessage(), e);
        }
    }

            @Override
    public void updateUser(UserDto userDto) throws SQLException {
        User user = userManagementMapper.convert(userDto);
        try {
            userRepository.updateUser(user);
        } catch (SQLException e) {
          throw new IllegalArgumentException("Database error while updating user");
        }

    }

    @Override
    public void deleteUser(String username) throws SQLException {
        try {
            userRepository.deleteUser(username);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Database error while deleting user: ", e);
        }
    }

    @Override
    public UserDto getByUsername(String username) throws SQLException {
        UserDto user = userManagementMapper.convert(userRepository.getByUsername(username));
        return user;
    }

    @Override
    public UserDto getById(Long id) throws SQLException {
        UserDto user = userManagementMapper.convert(userRepository.getById(id));
        return user;
    }

}
