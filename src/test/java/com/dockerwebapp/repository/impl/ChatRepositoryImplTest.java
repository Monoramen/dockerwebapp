package com.dockerwebapp.repository.impl;




import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.User;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.security.PrivateKey;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatRepositoryImplTest  extends AbstractDatabaseTest {
    private List<User> participants = new ArrayList<>();
    private ChatRepositoryImpl chatRepository;

    @BeforeEach
    void init() throws SQLException {
        initializeDatabaseConnection();
        chatRepository = new ChatRepositoryImpl();
        UserManagementRepositoryImpl userRepository = new UserManagementRepositoryImpl();
        User user1 = new User.UserBuilder(1L, "username", "password").build();
        User user2 = new User.UserBuilder(2L, "username2", "password").build();
        userRepository.createUser(user1);
        userRepository.createUser(user2);

        participants.add(user1);
        participants.add(user2);
    }

    private Chat createChat(Long id, String name) {
        return new Chat(id, name);
    }




    @Test
    void testAddChat() throws SQLException {
        Chat chat = new Chat();
        chat.setId(3L);
        chat.setName("My Chat");
        chat.setParticipantIds(participants);

        chatRepository.addChat(chat);

        List<Chat> chats = chatRepository.getUserChats(participants.get(0).getId());
        assertNotNull(chats);
        assertFalse(chats.isEmpty());
        assertEquals("My Chat", chats.get(chats.size() - 1).getName());

        // Clean up
        chatRepository.deleteChat(chat.getId());
    }

    @Test
    void testUpdateChat() throws SQLException {
        List<Chat> chats = chatRepository.getUserChats(1L);
        Chat chat = chats.get(0);
        chat.setId(1L);
        chat.setName("Updated Chat Name");
        chatRepository.updateChat(chat);
        assertEquals("Updated Chat Name", chats.get(0).getName());
    }


    @Test
    void testDeleteChat() throws SQLException {
        System.out.println(chatRepository.getUserChats(1L));
        Chat chat = new Chat();
        chat.setId(1L);
        chatRepository.deleteChat(chat.getId());
        chat.setId(2L);
        chatRepository.deleteChat(chat.getId());
        List<Chat> chats = chatRepository.getUserChats(1L);
        assertTrue(chats.isEmpty());
    }

    @Test
    void testDeleteNotExitsChat() throws SQLException {
        System.out.println(chatRepository.getUserChats(1L));
        Chat chat = new Chat();
        chat.setId(5L);
        assertThrows(SQLException.class, () -> chatRepository.deleteChat(chat.getId()));
    }

    @Test
    void testGetUserChats() throws SQLException {
        List<Chat> chats = chatRepository.getUserChats(1L);
        assertNotNull(chats);
        assertEquals(0, chats.size());
    }


    @Test
    void testGetUserChatsAtLeastTwoparticipants() {
        Chat chat1 = createChat(1L, "Chat One");
        assertThrows(SQLException.class, () -> chatRepository.addChat(chat1));
    }

}
