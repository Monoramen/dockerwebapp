package com.dockerwebapp.service;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.Message;
import com.dockerwebapp.model.User;
import com.dockerwebapp.repository.MessageRepository;
import com.dockerwebapp.service.impl.MessageServiceImpl;
import com.dockerwebapp.servlet.dto.MessageDto;
import com.dockerwebapp.servlet.mapper.MessageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
    private User sender;
    private User sender2;
    private Chat chatId;


    @BeforeEach
    void setUp() {
        messageRepository = Mockito.mock(MessageRepository.class);
        messageMapper = Mockito.mock(MessageMapper.class);
        messageService = new MessageServiceImpl(messageRepository);

        sender = new User.UserBuilder().setId(1L).build();
        sender2 = new User.UserBuilder().setId(2L).setUsername("name").setFirstName("fname").setLastName("lname").build();
        chatId = new Chat(1L, "Chat1");
    }

    @Test
    void testFindAll() throws SQLException {
        List<Message> messages = Arrays.asList(
                new Message(1L, "Hello, this is Jane", LocalDateTime.now(), sender, chatId),
                new Message(2L, "Hi Jane, this is Michael", LocalDateTime.now(), sender2, chatId)
        );

        when(messageRepository.findAll(chatId.getId())).thenReturn(messages);
        when(messageMapper.convert(any(Message.class))).thenAnswer(invocation -> {
            Message msg = invocation.getArgument(0);
            return new MessageDto(msg.getId(), msg.getText(), msg.getDateTime().toString(), msg.getSenderId().getId(), msg.getChatId().getId());
        });

        List<MessageDto> result = messageService.findAll(chatId.getId());

        assertEquals(messages.size(), result.size());
        verify(messageRepository).findAll(chatId.getId());
    }

    @Test
    void testFindByChatId() throws SQLException {

        List<Message> messages = Arrays.asList(
                new Message(1L, "Hello, this is Jane", LocalDateTime.now(), sender, chatId)
        );

        when(messageRepository.findByChatId(chatId.getId())).thenReturn(messages);
        List<MessageDto> result = messageService.findByChatId(chatId.getId());
        assertEquals(messages.size(), result.size());
        verify(messageRepository).findByChatId(chatId.getId());
    }

    @Test
    void testFindById() throws SQLException {
        Long id = 1L;
        Message message = new Message(id, "Hello", LocalDateTime.now(), sender, chatId);

        when(messageRepository.findById(id)).thenReturn(message);
        MessageDto result = messageService.findById(id);
        assertNotNull(result);
        assertEquals(message.getText(), result.getText());
        verify(messageRepository).findById(id);
    }


    @Test
    void testDelete() throws SQLException {
        Long id = 1L;
        messageService.delete(id);

        verify(messageRepository).delete(id);
    }
    @Test
    void testSave() throws SQLException {
        MessageDto messageDto = new MessageDto(3L, "New message", "2024-09-22 13:58:58", 1L, 1L);

        Message message = new Message();
        message.setText("New message");
        message.setSenderId(sender);
        message.setChatId(chatId);
        message.setDateTime(LocalDateTime.parse("2024-09-22 13:58:58", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        when(messageMapper.convert(messageDto)).thenReturn(message);
        messageService.save(messageDto);
        verify(messageRepository).save(argThat(savedMessage ->
                savedMessage.getText().equals("New message") &&
                        savedMessage.getSenderId().getId().equals(1L) &&
                        savedMessage.getChatId().getId().equals(1L) &&
                        savedMessage.getDateTime().equals(LocalDateTime.parse("2024-09-22 13:58:58",
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        ));
    }

    @Test
    void testUpdate() throws SQLException {
        MessageDto messageDto = new MessageDto(1L, "Updated message", "2024-09-22 13:58:58", sender2.getId(), chatId.getId());

        Message message = new Message();
        message.setId(1L);
        message.setText("Updated message");
        message.setSenderId(sender2);
        message.setChatId(chatId);

        message.setDateTime(LocalDateTime.parse("2024-09-22 13:58:58", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        when(messageMapper.convert(messageDto)).thenReturn(message);

        messageService.update(messageDto);

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).update(messageCaptor.capture());

        Message capturedMessage = messageCaptor.getValue();
        assertEquals(1L, capturedMessage.getId());
        assertEquals(1L, capturedMessage.getChatId().getId());
        assertEquals(2L, capturedMessage.getSenderId().getId());
        assertEquals("Updated message", capturedMessage.getText());
        assertEquals(LocalDateTime.parse("2024-09-22 13:58:58", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), capturedMessage.getDateTime());

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