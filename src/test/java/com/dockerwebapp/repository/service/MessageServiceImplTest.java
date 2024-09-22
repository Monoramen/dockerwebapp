package com.dockerwebapp.repository.service;

import com.dockerwebapp.model.Message;
import com.dockerwebapp.repository.MessageRepository;
import com.dockerwebapp.service.impl.MessageServiceImpl;
import com.dockerwebapp.servlet.dto.MessageDto;
import com.dockerwebapp.servlet.mapper.MessageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

    private MessageServiceImpl messageService;
    private MessageRepository messageRepository;
    private MessageMapper messageMapper;

    @BeforeEach
    void setUp() {
        messageRepository = Mockito.mock(MessageRepository.class);
        messageMapper = Mockito.mock(MessageMapper.class);
        messageService = new MessageServiceImpl(messageRepository); // Внедряем моки через конструктор
    }

    @Test
    void testFindAll() throws SQLException {
        // Arrange
        Long chatId = 1L;
        List<Message> messages = Arrays.asList(
                new Message(1L, "Hello, this is Jane", LocalDateTime.now(), 1L, chatId),
                new Message(2L, "Hi Jane, this is Michael", LocalDateTime.now(), 2L, chatId)
        );

        when(messageRepository.findAll(chatId)).thenReturn(messages);
        when(messageMapper.convert(any(Message.class))).thenAnswer(invocation -> {
            Message msg = invocation.getArgument(0);
            return new MessageDto(msg.getId(), msg.getText(), msg.getDateTime().toString(), msg.getSenderId(), msg.getChatId());
        });

        // Act
        List<MessageDto> result = messageService.findAll(chatId);

        // Assert
        assertEquals(messages.size(), result.size());
        verify(messageRepository).findAll(chatId); // Проверяем вызов метода findAll с правильным ID
    }

    @Test
    void testFindByChatId() throws SQLException {
        // Arrange
        Long chatId = 1L;
        List<Message> messages = Arrays.asList(
                new Message(1L, "Hello, this is Jane", LocalDateTime.now(), 1L, chatId)
        );

        when(messageRepository.findByChatId(chatId)).thenReturn(messages);

        // Act
        List<MessageDto> result = messageService.findByChatId(chatId);

        // Assert
        assertEquals(messages.size(), result.size());
        verify(messageRepository).findByChatId(chatId); // Проверяем вызов метода findByChatId с правильным ID
    }

    @Test
    void testFindById() throws SQLException {
        // Arrange
        Long id = 1L;
        Message message = new Message(id, "Hello", LocalDateTime.now(), 1L, 1L);

        when(messageRepository.findById(id)).thenReturn(message);

        // Act
        MessageDto result = messageService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(message.getText(), result.getText());
        verify(messageRepository).findById(id); // Проверяем вызов метода findById с правильным ID
    }


    @Test
    void testDelete() throws SQLException {
        // Arrange
        Long id = 1L;

        // Act
        messageService.delete(id);

        // Assert
        verify(messageRepository).delete(id); // Проверяем вызов метода delete с правильным ID сообщения
    }
    @Test
    void testSave() throws SQLException {
        // Arrange
        MessageDto messageDto = new MessageDto(3L, "New message", "2024-09-22 13:58:58", 2L, 1L);

        Message message = new Message();
        message.setText("New message");
        message.setSenderId(2L);
        message.setChatId(1L);
        message.setDateTime(LocalDateTime.parse("2024-09-22 13:58:58", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        when(messageMapper.convert(messageDto)).thenReturn(message);

        // Act
        messageService.save(messageDto);

        // Assert
        verify(messageRepository).save(argThat(savedMessage ->
                savedMessage.getText().equals("New message") &&
                        savedMessage.getSenderId().equals(2L) &&
                        savedMessage.getChatId().equals(1L) &&
                        savedMessage.getDateTime().equals(LocalDateTime.parse("2024-09-22 13:58:58", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        )); // Проверяем вызов метода save с правильным объектом сообщения
    }

    @Test
    void testUpdate() throws SQLException {
        // Arrange
        MessageDto messageDto = new MessageDto(1L, "Updated message", "2024-09-22 13:58:58", 2L, 1L);

        Message message = new Message();
        message.setId(1L);
        message.setText("Updated message");
        message.setSenderId(2L);
        message.setChatId(1L);
        message.setDateTime(LocalDateTime.parse("2024-09-22 13:58:58", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        when(messageMapper.convert(messageDto)).thenReturn(message);

        // Act
        messageService.update(messageDto);

        // Assert
        verify(messageRepository).update(message); // Проверяем вызов метода update с правильным объектом сообщения
    }
}