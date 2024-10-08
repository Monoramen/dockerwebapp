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
    private User sender;
    private User sender2;
    private Chat  chatid;

    @BeforeEach
    void init()  throws SQLException {
        initializeDatabaseConnection();
        messageRepository = new MessageRepositoryImpl();
        sender = new User.UserBuilder().setId(1L).build();
        sender2 = new User.UserBuilder().setId(2L).build();
        chatid = new Chat(1L, "Chat1");
    }

    @Test
    void testFindAllMessage() throws SQLException {
        List<Message> savedMessage = messageRepository.findAll(1L);
        assertNotNull(savedMessage);
    }

    @Test
    void testFindById() throws SQLException {
        Message savedMessage = messageRepository.findById(1L);
        String datetime = savedMessage.getDateTime().toString();
        assertEquals("1", savedMessage.getId().toString());
        assertEquals("Hello, this is Jane", savedMessage.getText());
        assertEquals(1L, savedMessage.getSenderId().getId());
        assertEquals(1L, savedMessage.getChatId().getId());
        assertEquals(datetime, savedMessage.getDateTime().toString());
    }

    @Test
    void testSaveMessage() throws SQLException {
        Message message = new Message();
        message.setText("Hello, world!");
        message.setSenderId(sender);
        message.setChatId(chatid);
        message.setDateTime(LocalDateTime.now());
        messageRepository.save(message);
        List<Message> savedMessage = messageRepository.findAll(1L);
        assertEquals(3, savedMessage.size());
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
        message.setSenderId(sender);
        message.setChatId(chatid);
        message.setDateTime(LocalDateTime.now());
        messageRepository.save(message);
        messageRepository.delete(5L);
        Message deletedMessage = messageRepository.findById(5L);
        assertNull(deletedMessage);
    }

    @Test
    void testFindByChatId() throws SQLException {
        List<Message> messages = messageRepository.findByChatId(1L);

        assertNotNull(messages);
        assertFalse(messages.isEmpty(), "Messages list should not be empty");

        for (Message message : messages) {
            System.out.println(message);
        }

        assertEquals(2, messages.size());
    }
    @Test
    void testFindAllByChatId() throws SQLException {
        List<Message> expectedMessages = List.of(
                new Message(1L, "Hello, this is Jane",
                        LocalDateTime.of(2024, 9, 22, 18, 38, 52), sender, chatid),
                new Message(2L, "Hi Jane, this is Michael",
                        LocalDateTime.of(2024, 9, 22, 18, 38, 52), sender2, chatid)
        );

        List<Message> actualMessages = messageRepository.findAll(1L);

        assertEquals(expectedMessages.size(), actualMessages.size(), "Message sizes should match");

        for (int i = 0; i < expectedMessages.size(); i++) {
            Message expected = expectedMessages.get(i);
            Message actual = actualMessages.get(i);

            assertEquals(expected.getId(), actual.getId(), "Message ID should match");
            assertEquals(expected.getText(), actual.getText(), "Message text should match");
            assertEquals(expected.getSenderId().getId(), actual.getSenderId().getId(), "Sender ID should match");
            assertEquals(expected.getChatId().getId(), actual.getChatId().getId(), "Chat ID should match");

        }
    }


}