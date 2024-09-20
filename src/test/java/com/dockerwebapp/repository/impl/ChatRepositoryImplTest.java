package com.dockerwebapp.repository.impl;




import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.User;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.SQLException;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatRepositoryImplTest  extends AbstractDatabaseTest {
    private UserRepositoryImpl userRepository;
    private ChatRepositoryImpl chatRepository;

    @BeforeEach
    void init() throws SQLException {
        initializeDatabaseConnection();
        userRepository = new UserRepositoryImpl();
        chatRepository = new ChatRepositoryImpl();
    }

    private Chat createChat(Long id, String name) {
        return new Chat(id, name);
    }

    @Test
    void testAddChat() throws SQLException {
        Chat chat = new Chat();
        chat.setId(3L);
        chat.setName("My Chat");

        User user1 = new User.UserBuilder(1L, "first", "1234").build();
        User user2 = new User.UserBuilder(2L, "second", "4321").build();
        chat.addParticipant(user1);
        chat.addParticipant(user2);
        chatRepository.addChat(chat);
        List<Chat> chats = userRepository.getUserChats(1L);
        assertNotNull(chats);
        assertFalse(chats.isEmpty());
        assertEquals("My Chat", chats.get(2).getName());
       chatRepository.deleteChat(chat);
    }

    @Test
    void testDeleteChat() throws SQLException {
        System.out.println(chatRepository.getUserChats(1L));
        Chat chat = new Chat();
        chat.setId(1L);
        chatRepository.deleteChat(chat);
        chat.setId(2L);
        chatRepository.deleteChat(chat);
        List<Chat> chats = userRepository.getUserChats(1L); // Проверяем, что чат был удален
        assertTrue(chats.isEmpty());
    }

    @Test
    void testGetUserChats() throws SQLException {
        List<Chat> chats = userRepository.getUserChats(1L); // Получаем чаты пользователя с id=1
        assertNotNull(chats);

        assertEquals(0, chats.size()); // Проверяем количество чатов
    }


    @Test
    void testGetUserChatsAtLeastTwoparticipants() {
        Chat chat1 = createChat(1L, "Chat One");
        assertThrows(SQLException.class, () -> chatRepository.addChat(chat1)); // Проверяем количество чатов
    }


    @Test
    void testUpdateChat() throws SQLException {
        List<Chat> chats = chatRepository.getUserChats(1L);

        Chat chat = chats.get(0);
        chat.setId(1L);

        chat.setName("Updated Chat Name");
        chatRepository.updateChat(chat);

        assertEquals("Updated Chat Name", chats.get(0).getName()); // Проверяем обновленное имя чата
    }

}
