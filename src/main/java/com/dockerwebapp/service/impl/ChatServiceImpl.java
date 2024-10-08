package com.dockerwebapp.service.impl;


import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.User;
import com.dockerwebapp.repository.ChatRepository;

import com.dockerwebapp.repository.UserManagementRepository;
import com.dockerwebapp.repository.impl.ChatRepositoryImpl;
import com.dockerwebapp.repository.impl.UserManagementRepositoryImpl;
import com.dockerwebapp.service.ChatService;
import com.dockerwebapp.servlet.dto.ChatDto;
import com.dockerwebapp.servlet.dto.ChatDtoParticipant;
import com.dockerwebapp.servlet.mapper.ChatsMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatServiceImpl implements ChatService {
    private  ChatRepository chatRepository;
    private  UserManagementRepository userManagementRepository;


    public ChatServiceImpl() {
        this.chatRepository = new ChatRepositoryImpl();
        this.userManagementRepository = new UserManagementRepositoryImpl();
    }

    public ChatServiceImpl(ChatRepository chatRepository,
                           UserManagementRepository userManagementRepository) {
        this.chatRepository = chatRepository;
        this.userManagementRepository = userManagementRepository;
    }


    public void addChat(ChatDtoParticipant chatDto) throws SQLException {
        Chat chat = new Chat();
        chat.setId(chatDto.getId());
        chat.setName(chatDto.getName());
        List<User> participants = new ArrayList<>();

        if (chatDto.getParticipantIds() != null) {
            for (Long participantId : chatDto.getParticipantIds()) {
                if (userManagementRepository.getById(participantId) != null) {
                    participants.add(userManagementRepository.getById(participantId));
                }
                else {
                    throw new SQLException("User with ID " + participantId + " does not exist.");
                }
            }
            if (participants.size() >=2){
                chat.setParticipantIds(participants);
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
            throw new IllegalArgumentException("Error updating chat");
        }

    }

    @Override
    public void deleteChat(Long chatId) throws SQLException {

        try {
            chatRepository.deleteChat(chatId);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error deleting chat");
        }
    }


    @Override
    public ChatDto getChatById(Long chatId)  {
        Chat chat;
        try {
            chat = chatRepository.getChatById(chatId);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error fetching chat by id");
        }
        return  ChatsMapper.INSTANCE.convert(chat);
    }

    @Override
    public List<ChatDto> getUserChats(Long userId) throws SQLException {
        List<Chat> chats;
        try {
            chats = chatRepository.getUserChats(userId);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error fetching user chats");
        }
        return chats.stream()
                .map(chat -> ChatsMapper.INSTANCE.convert(chat))
                .collect(Collectors.toList());
    }

}