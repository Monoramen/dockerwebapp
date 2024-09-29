package com.dockerwebapp.service;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.repository.ChatRepository;
import com.dockerwebapp.service.impl.ChatServiceImpl;
import com.dockerwebapp.servlet.dto.ChatDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    private ChatServiceImpl chatService;
    private ChatRepository chatRepository;

    @BeforeEach
    void setUp() {
        chatRepository = Mockito.mock(ChatRepository.class);
        chatService = new ChatServiceImpl(chatRepository); // Внедряем
    }

    @Test
    void testAddChat() throws SQLException {
        ChatDto chatDto = new ChatDto(1L, "chat1", Arrays.asList(1L, 2L));
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
        ChatDto chatDto = new ChatDto(1L, "updatedChat", Arrays.asList(1L, 2L));
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
        ChatDto chatDto = new ChatDto(1L, "chat1", Arrays.asList(1L, 2L));
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
        ChatDto chatDto = new ChatDto(1L, "updatedChat", Arrays.asList(1L, 2L));
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