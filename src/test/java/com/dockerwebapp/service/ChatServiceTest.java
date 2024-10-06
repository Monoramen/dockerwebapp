package com.dockerwebapp.service;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.User;
import com.dockerwebapp.repository.ChatRepository;
import com.dockerwebapp.repository.UserManagementRepository;
import com.dockerwebapp.repository.impl.UserManagementRepositoryImpl;
import com.dockerwebapp.service.impl.ChatServiceImpl;
import com.dockerwebapp.servlet.dto.ChatDto;

import com.dockerwebapp.servlet.dto.ChatDtoParticipant;
import com.dockerwebapp.servlet.dto.UserDto;
import com.dockerwebapp.servlet.dto.UserInfoDto;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    private ChatServiceImpl chatService;
    private ChatRepository chatRepository;
    private UserManagementRepository userRepository;
    private List<Long> participants = new ArrayList<>();
    @BeforeEach
    void setUp() throws SQLException {
        chatRepository = Mockito.mock(ChatRepository.class);
        userRepository = Mockito.mock(UserManagementRepository.class);
        chatService = new ChatServiceImpl(chatRepository,userRepository); // Внедряем

        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setUsername("name1");
        userDto1.setPassword("pass");

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setUsername("name2");
        userDto2.setPassword("pass");
        List<Long> participants = new ArrayList<>();
        participants.add(userDto1.getId());
        participants.add(userDto2.getId());

        User user1 = new User.UserBuilder(1L, "name1","pass").build();
        User user2 = new User.UserBuilder(2L, "name2","pass").build();

        when(userRepository.getById(1L)).thenReturn(user1);
        when(userRepository.getById(2L)).thenReturn(user2);

    }

    @Test
    void testAddChat() throws SQLException {
        ChatDtoParticipant chatDto = new ChatDtoParticipant(1L, "chat1", participants);
        chatService.addChat(chatDto);
        verify(chatRepository).addChat(any(Chat.class));
    }

    @Test
    void testDeleteChat() throws SQLException {
        Long chatId = 1L;
        chatService.deleteChat(chatId);
        verify(chatRepository).deleteChat(chatId);
    }

    @Test
    void testUpdateChat() throws SQLException {

        UserInfoDto userDto1 = new UserInfoDto();
        userDto1.setId(1L);
        userDto1.setUsername("name1");

        UserInfoDto userDto2 = new UserInfoDto();
        userDto2.setId(2L);
        userDto2.setUsername("name2");

        List<UserInfoDto> participants = new ArrayList<>();
        participants.add(userDto1);
        participants.add(userDto2);
        ChatDto chatDto = new ChatDto(1L, "updatedChat", participants);
        chatService.updateChat(chatDto);
        verify(chatRepository).updateChat(any(Chat.class));
    }

    @Test
    void testGetChatById() throws SQLException {
        // Arrange
        Long chatId = 1L;
        Chat expectedChat = new Chat();
        expectedChat.setId(chatId);
        when(chatRepository.getChatById(chatId)).thenReturn(expectedChat);
        ChatDto actualChat = chatService.getChatById(chatId);
        assertEquals(expectedChat.getId(), actualChat.getId());
    }

    @Test
    void testGetUserChats() throws SQLException {
        Long userId = 1L;
        List<Chat> chats = Arrays.asList(new Chat(), new Chat());
        when(chatRepository.getUserChats(userId)).thenReturn(chats);
        List<ChatDto> result = chatService.getUserChats(userId);
        assertEquals(chats.size(), result.size());
        verify(chatRepository).getUserChats(userId);
    }

    @Test
    void testAddChatThrowException() throws SQLException {
        ChatDtoParticipant chatDto = new ChatDtoParticipant(1L, "chat1",participants);
        doThrow(new SQLException("Error adding chat")).when(chatRepository).addChat(any(Chat.class));

        assertThrows(SQLException.class, () -> chatService.addChat(chatDto));
        verify(chatRepository).addChat(any(Chat.class));
    }


    @Test
    void testDeleteChatThrowException() throws SQLException {
        Long chatId = 1L;
        doThrow(new SQLException("Error deleting chat")).when(chatRepository).deleteChat(chatId);
        assertThrows(IllegalArgumentException.class, () -> chatService.deleteChat(chatId));
        verify(chatRepository).deleteChat(chatId);
    }

    @Test
    void testUpdateChatThrowException() throws SQLException {

        UserInfoDto userDto1 = new UserInfoDto();
        userDto1.setId(1L);
        userDto1.setUsername("name1");

        UserInfoDto userDto2 = new UserInfoDto();
        userDto2.setId(2L);
        userDto2.setUsername("name2");
        List<UserInfoDto> participants = new ArrayList<>();
        participants.add(userDto1);
        participants.add(userDto2);

        ChatDto chatDto = new ChatDto(1L, "updatedChat",participants);
        doThrow(new SQLException("Error updating chat")).when(chatRepository).updateChat(any(Chat.class));
        assertThrows(IllegalArgumentException.class, () -> chatService.updateChat(chatDto));
        verify(chatRepository).updateChat(any(Chat.class));
    }


    @Test
    void testGetChatByIdThrowException() throws SQLException {
        Long chatId = 1L;
        doThrow(new SQLException("Error fetching chat by id")).when(chatRepository).getChatById(chatId);
        assertThrows(IllegalArgumentException.class, () -> chatService.getChatById(chatId));
        verify(chatRepository).getChatById(chatId);
    }

    @Test
    void testGetUserChatsThrowException() throws SQLException {
        Long userId = 1L;
        doThrow(new SQLException("Error fetching user chats")).when(chatRepository).getUserChats(userId);
        assertThrows(IllegalArgumentException.class, () -> chatService.getUserChats(userId));
        verify(chatRepository).getUserChats(userId);
    }
}