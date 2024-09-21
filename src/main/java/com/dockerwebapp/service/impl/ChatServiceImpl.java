package com.dockerwebapp.service.impl;


import com.dockerwebapp.model.Chat;
import com.dockerwebapp.repository.ChatRepository;

import com.dockerwebapp.repository.impl.ChatRepositoryImpl;
import com.dockerwebapp.service.ChatService;
import com.dockerwebapp.servlet.dto.ChatDto;
import com.dockerwebapp.servlet.mapper.ChatsMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ChatServiceImpl implements ChatService {
    private ChatRepository chatRepository;
    private ChatsMapper chatsMapper = ChatsMapper.INSTANCE;

    public ChatServiceImpl() {
        this.chatRepository =  new ChatRepositoryImpl();

    }

    @Override
    public void addChat(ChatDto chatDto) throws SQLException {
        Chat chat = ChatsMapper.INSTANCE.convert(chatDto);
        chatRepository.addChat(chat);
    }

    @Override
    public void deleteChat(ChatDto chatDto) throws SQLException {
        Chat chat = ChatsMapper.INSTANCE.convert(chatDto);
        chatRepository.deleteChat(chat);
    }


    @Override
    public void updateChat(ChatDto chatDto) throws SQLException {
        Chat chat = ChatsMapper.INSTANCE.convert(chatDto);
        chatRepository.updateChat(chat);
    }

    @Override
    public Chat getChatById(Long chatId)  {
        try {
            return chatRepository.getChatById(chatId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ChatDto> getUserChats(Long userId) throws SQLException {
        List<Chat> chats = chatRepository.getUserChats(userId);
        return chats.stream()
                .map(chat -> ChatsMapper.INSTANCE.convert(chat)) // Используем маппер для каждого объекта Chat
                .collect(Collectors.toList());
    }

}