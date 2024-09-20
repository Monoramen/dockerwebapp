package com.dockerwebapp.service.impl;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.repository.UserRepository;
import com.dockerwebapp.repository.impl.UserRepositoryImpl;
import com.dockerwebapp.service.UserService;
import com.dockerwebapp.servlet.dto.ChatDto;
import com.dockerwebapp.servlet.mapper.ChatMapper;
import com.dockerwebapp.servlet.mapper.UserMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ChatMapper chatMapper = ChatMapper.INSTANCE;  // Используйте правильный маппер
    private UserMapper userMapper = UserMapper.INSTANCE;  // Используйте правильный маппер

    public UserServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
    }

    @Override
    public List<ChatDto> getUserChats(Long userId) throws SQLException {
        List<Chat> chats;
        try {
            chats = userRepository.getUserChats(userId);
            System.out.println("Chats retrieved: " + chats);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return chats.stream().map(chatMapper::convert).collect(Collectors.toList());  // Используем chatMapper для конвертации
    }


}

