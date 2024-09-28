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
    private transient ChatRepository chatRepository;


    public ChatServiceImpl() {
        this.chatRepository =  new ChatRepositoryImpl();
    }

    public ChatServiceImpl(ChatRepository chatRepository) { // Конструктор для внедрения зависимости
        this.chatRepository = chatRepository;
    }

    @Override
    public void addChat(ChatDto chatDto) throws SQLException {
        Chat chat = new Chat();
        chat.setId(chatDto.getId());
        chat.setName(chatDto.getName());

        if (chatDto.getParticipantIds() != null) {
            for (Long participantId : chatDto.getParticipantIds()) {
                chat.addParticipant(participantId);
            }
        }
        chatRepository.addChat(chat);
    }

    @Override
    public void updateChat(ChatDto chatDto) throws SQLException {
        Chat chat = ChatsMapper.INSTANCE.convert(chatDto);
        try {
            chatRepository.updateChat(chat);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteChat(Long chatId) throws SQLException {

        try {
            chatRepository.deleteChat(chatId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public ChatDto getChatById(Long chatId)  {
        Chat chat;
        try {
            chat = chatRepository.getChatById(chatId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  ChatsMapper.INSTANCE.convert(chat);
    }

    @Override
    public List<ChatDto> getUserChats(Long userId) throws SQLException {
        List<Chat> chats = chatRepository.getUserChats(userId);
        return chats.stream()
                .map(chat -> ChatsMapper.INSTANCE.convert(chat)) // Используем маппер для каждого объекта Chat
                .collect(Collectors.toList());
    }

}