package com.dockerwebapp.repository.service;

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
        // Arrange
        ChatDto chatDto = new ChatDto(1L, "chat1", Arrays.asList(1L, 2L));

        // Act
        chatService.addChat(chatDto);

        // Assert
        verify(chatRepository).addChat(any(Chat.class)); // Проверяем, что метод addChat был вызван
    }

    @Test
    void testDeleteChat() throws SQLException {
        // Arrange
        Long chatId = 1L;

        // Act
        chatService.deleteChat(chatId);

        // Assert
        verify(chatRepository).deleteChat(chatId); // Проверяем, что метод deleteChat был вызван с правильным ID
    }

    @Test
    void testUpdateChat() throws SQLException {
        // Arrange
        ChatDto chatDto = new ChatDto(1L, "updatedChat", Arrays.asList(1L, 2L));

        // Act
        chatService.updateChat(chatDto);

        // Assert
        verify(chatRepository).updateChat(any(Chat.class)); // Проверяем, что метод updateChat был вызван
    }

    @Test
    void testGetChatById() throws SQLException {
        // Arrange
        Long chatId = 1L;
        Chat expectedChat = new Chat();
        expectedChat.setId(chatId);

        when(chatRepository.getChatById(chatId)).thenReturn(expectedChat); // Настраиваем мок

        Chat actualChat = chatService.getChatById(chatId);

        // Assert
        assertEquals(expectedChat.getId(), actualChat.getId()); // Проверяем, что полученный чат соответствует ожидаемому
    }

    @Test
    void testGetUserChats() throws SQLException {
        // Arrange
        Long userId = 1L;
        List<Chat> chats = Arrays.asList(new Chat(), new Chat());

        when(chatRepository.getUserChats(userId)).thenReturn(chats); // Настраиваем мок

        // Act
        List<ChatDto> result = chatService.getUserChats(userId);

        // Assert
        assertEquals(chats.size(), result.size()); // Проверяем количество чатов
        verify(chatRepository).getUserChats(userId); // Проверяем вызов метода getUserChats с правильным ID пользователя
    }
}