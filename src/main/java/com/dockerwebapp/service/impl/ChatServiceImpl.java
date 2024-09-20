package com.dockerwebapp.service.impl;


import com.dockerwebapp.model.Chat;
import com.dockerwebapp.repository.ChatRepository;

import com.dockerwebapp.repository.impl.ChatRepositoryImpl;
import com.dockerwebapp.service.ChatService;
import com.dockerwebapp.servlet.dto.ChatDto;
import com.dockerwebapp.servlet.mapper.ChatMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ChatServiceImpl implements ChatService {
    private ChatRepository chatRepository = new ChatRepositoryImpl();

    public ChatServiceImpl() {
        this.chatRepository = chatRepository;
    }

    @Override
    public void addChat(ChatDto chatDto) throws SQLException {
        Chat chat = ChatMapper.INSTANCE.convert(chatDto);
        chatRepository.addChat(chat);
    }

    @Override
    public void deleteChat(ChatDto chatDto) throws SQLException {
        Chat chat = ChatMapper.INSTANCE.convert(chatDto);
        chatRepository.deleteChat(chat);
    }

    @Override
    public List<ChatDto> getUserChats(Long userId) throws SQLException {
        List<Chat> chats = chatRepository.getUserChats(userId);
        return chats.stream()
                .map(ChatMapper.INSTANCE::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void updateChat(ChatDto chatDto) throws SQLException {
        Chat chat = ChatMapper.INSTANCE.convert(chatDto);
        chatRepository.updateChat(chat);
    }

    @Override
    public Chat getChatById(Long chatId) {
        return null;
    }
}