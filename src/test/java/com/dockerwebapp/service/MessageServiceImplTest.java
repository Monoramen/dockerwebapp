package com.dockerwebapp.service;

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
        Long chatId = 1L;
        List<Message> messages = Arrays.asList(
                new Message(1L, "Hello, this is Jane", LocalDateTime.now(), 1L, chatId)
        );

        when(messageRepository.findByChatId(chatId)).thenReturn(messages);
        List<MessageDto> result = messageService.findByChatId(chatId);
        assertEquals(messages.size(), result.size());
        verify(messageRepository).findByChatId(chatId); // Проверяем вызов метода findByChatId с правильным ID
    }

    @Test
    void testFindById() throws SQLException {
        Long id = 1L;
        Message message = new Message(id, "Hello", LocalDateTime.now(), 1L, 1L);

        when(messageRepository.findById(id)).thenReturn(message);
        MessageDto result = messageService.findById(id);
        assertNotNull(result);
        assertEquals(message.getText(), result.getText());
        verify(messageRepository).findById(id); // Проверяем вызов метода findById с правильным ID
    }


    @Test
    void testDelete() throws SQLException {
        Long id = 1L;
        messageService.delete(id);

        verify(messageRepository).delete(id);
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
        messageService.save(messageDto);
        verify(messageRepository).save(argThat(savedMessage ->
                savedMessage.getText().equals("New message") &&
                        savedMessage.getSenderId().equals(2L) &&
                        savedMessage.getChatId().equals(1L) &&
                        savedMessage.getDateTime().equals(LocalDateTime.parse("2024-09-22 13:58:58", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        ));
    }

    @Test
    void testUpdate() throws SQLException {
        MessageDto messageDto = new MessageDto(1L, "Updated message", "2024-09-22 13:58:58", 2L, 1L);
        Message message = new Message();
        message.setId(1L);
        message.setText("Updated message");
        message.setSenderId(2L);
        message.setChatId(1L);
        message.setDateTime(LocalDateTime.parse("2024-09-22 13:58:58", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        when(messageMapper.convert(messageDto)).thenReturn(message);
        messageService.update(messageDto);
        verify(messageRepository).update(message); // Проверяем вызов метода update с правильным объектом сообщения
    }



    @Test
    void testFindAllThrowsSQLException() throws SQLException {
        doThrow(new SQLException("Database error")).when(messageRepository).findAll(any(Long.class));
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.findAll(1L);
        });
    }

    @Test
    void testFindByChatId_ThrowsSQLException() throws SQLException {
        doThrow(new SQLException("Database error")).when(messageRepository).findByChatId(any(Long.class));
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.findByChatId(1L);
        });
    }

    @Test
    void testFindById_ThrowsSQLException() throws SQLException {
        doThrow(new SQLException("Database error")).when(messageRepository).findById(any(Long.class));
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.findById(1L);
        });
    }

    @Test
    void testSave_ThrowsSQLException() throws SQLException {
        Message message = new Message();
        doThrow(new SQLException("Database error")).when(messageRepository).save(any(Message.class));
        MessageDto messageDto = new MessageDto();
        when(messageMapper.convert(any(MessageDto.class))).thenReturn(message);
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.save(messageDto);
        });
    }

    @Test
    void testUpdate_ThrowsSQLException() throws SQLException {
        Message message = new Message();
        doThrow(new SQLException("Database error")).when(messageRepository).update(any(Message.class));
        MessageDto messageDto = new MessageDto();
        when(messageMapper.convert(any(MessageDto.class))).thenReturn(message);
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.update(messageDto);
        });
    }

    @Test
    void testDeleteThrowsSQLException() throws SQLException {
        doThrow(new SQLException("Database error")).when(messageRepository).delete(any(Long.class));
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.delete(1L);
        });
    }



    @Test
    void testFindAllThrowException() throws SQLException {
        Long chatId = 1L;
        when(messageRepository.findAll(chatId)).thenThrow(new SQLException("Error fetching messages"));
        assertThrows(IllegalArgumentException.class, () -> messageService.findAll(chatId));
        verify(messageRepository).findAll(chatId);
    }

    @Test
    void testFindByChatIdThrowException() throws SQLException {
        Long chatId = 1L;
        when(messageRepository.findByChatId(chatId)).thenThrow(new SQLException("Error fetching messages by chatId"));
        assertThrows(IllegalArgumentException.class, () -> messageService.findByChatId(chatId));
        verify(messageRepository).findByChatId(chatId);
    }

    @Test
    void testFindByIdThrowException() throws SQLException {
        Long messageId = 1L;
        when(messageRepository.findById(messageId)).thenThrow(new SQLException("Error fetching message by ID"));
        assertThrows(IllegalArgumentException.class, () -> messageService.findById(messageId));
        verify(messageRepository).findById(messageId);
    }

    @Test
    void testSave_ShouldThrowExceptionWhenSQLExceptionOccurs() throws SQLException {
        MessageDto messageDto = new MessageDto(1L, "Test message", "2022-01-01 00:00:00", 1L, null);
        doThrow(new SQLException("Error saving message")).when(messageRepository).save(any(Message.class));
        assertThrows(IllegalArgumentException.class, () -> messageService.save(messageDto));
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void testUpdateThrowException() throws SQLException {
        MessageDto messageDto = new MessageDto(1L, "Updated message", "2022-01-01 00:00:00", 1L, null);
        doThrow(new SQLException("Error updating message")).when(messageRepository).update(any(Message.class));
        assertThrows(IllegalArgumentException.class, () -> messageService.update(messageDto));
        verify(messageRepository).update(any(Message.class));
    }

    @Test
    void testDeleteThrowException() throws SQLException {
        Long messageId = 1L;
        doThrow(new SQLException("Error deleting message")).when(messageRepository).delete(messageId);
        assertThrows(IllegalArgumentException.class, () -> messageService.delete(messageId));
        verify(messageRepository).delete(messageId);
    }


}