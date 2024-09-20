package com.dockerwebapp.repository.impl;


import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.Message;
import com.dockerwebapp.model.User;

import org.junit.jupiter.api.*;

import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageRepositoryImplTest  extends AbstractDatabaseTest {
    private MessageRepositoryImpl messageRepository;

    @BeforeEach
    void init()  throws SQLException {
        initializeDatabaseConnection();
        messageRepository = new MessageRepositoryImpl();
    }

    @Test
    void testSaveAndFindMessage() throws SQLException {
        List<Message> savedMessage = messageRepository.findAll();
        assertNotNull(savedMessage);
    }

    @Test
    void testFindById() throws SQLException {
        Message savedMessage = messageRepository.findById(1L);
        String datetime = savedMessage.getDateTime().toString();
        assertEquals("1", savedMessage.getId().toString());
        assertEquals("Hello, this is Jane", savedMessage.getText());
        assertEquals("first", savedMessage.getSender().getUsername());
        assertEquals("chat1", savedMessage.getChat().getName());
        assertEquals(datetime, savedMessage.getDateTime().toString());
    }

    @Test
    void testSaveMessage() throws SQLException {
        Message message = new Message();
        message.setText("Hello, world!");
        message.setSender(new User.UserBuilder(1L, "first", "1234").build());
        message.setChat(new Chat(2L, "chat1"));
        message.setDateTime(LocalDateTime.now());
        messageRepository.save(message);
        List<Message> savedMessage = messageRepository.findAll();
        assertEquals(5, savedMessage.size());
    }

    @Test
    void testUpdateMessage() throws SQLException {
        Message message = new Message();
        message.setText("Changed text");
        message.setDateTime(LocalDateTime.now());
        message.setId(4L);
        messageRepository.update(message);
        Message updatedMessage = messageRepository.findById(4L);
        assertEquals("Changed text", updatedMessage.getText());
        assertTrue(Duration.between(message.getDateTime(), updatedMessage.getDateTime()).getSeconds() < 1,
                "The date times should be within 1 second of each other");
        assertEquals(message.getId(), updatedMessage.getId());
    }

    @Test
    void testDeleteMessage() throws SQLException {
        Message message = new Message();
        message.setText("Hello, world!");
        message.setSender(new User.UserBuilder(1L, "first", "1234").build());
        message.setChat(new Chat(2L, "chat1"));
        message.setDateTime(LocalDateTime.now());
        messageRepository.save(message);
        messageRepository.delete(5L);
        Message deletedMessage = messageRepository.findById(5L);
        assertNull(deletedMessage);
    }

    @Test
    void testFindByChatId() throws SQLException {
        // Предполагаем, что в базе данных уже есть данные для чата с id = 1
        List<Message> messages = messageRepository.findByChatId(1L);

        // Проверяем, что сообщения не пустые
        assertNotNull(messages);
        assertFalse(messages.isEmpty(), "Messages list should not be empty");

        // Выводим сообщения для проверки
        for (Message message : messages) {
            System.out.println(message);
        }

        // Дополнительные проверки на содержание сообщений
        assertEquals(2, messages.size()); // Предполагаем, что в чате 1 два сообщения
    }

}