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
    private ChatRepositoryImpl chatRepository;

    @BeforeEach
    void init() throws SQLException {
        initializeDatabaseConnection();
        chatRepository = new ChatRepositoryImpl();
    }

    private Chat createChat(Long id, String name) {
        return new Chat(id, name);
    }

    @Test
    void testAddChat() throws SQLException {
        // Arrange
        Chat chat = new Chat();
        chat.setId(3L);
        chat.setName("My Chat");
        chat.addParticipant(1L);
        chat.addParticipant(2L);
        chatRepository.addChat(chat);
        List<Chat> chats = chatRepository.getUserChats(2L);
        assertNotNull(chats);
        assertFalse(chats.isEmpty());
        assertEquals("My Chat", chats.get(chats.size() - 1).getName()); // Проверяем имя последнего добавленного чата
        chatRepository.deleteChat(3L);
    }

    @Test
    void testDeleteChat() throws SQLException {
        Chat chat = new Chat();
        chat.setId(1L);
        chatRepository.deleteChat(chat.getId());
        chat.setId(2L);
        chatRepository.deleteChat(chat.getId());
        List<Chat> chats = chatRepository.getUserChats(1L); // Проверяем, что чат был удален
        assertTrue(chats.isEmpty());
    }

    @Test
    void testDeleteNotExitsChat() throws SQLException {
        Chat chat = new Chat();
        chat.setId(5L);
        assertThrows(SQLException.class, () -> chatRepository.deleteChat(chat.getId()));
    }

    @Test
    void testGetUserChats() throws SQLException {
        List<Chat> chats = chatRepository.getUserChats(1L); // Получаем чаты пользователя с id=1
        assertNotNull(chats);
        assertEquals(0, chats.size()); // Проверяем количество чатов
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

    @Test
    void testGetUserChatsAtLeastTwoparticipants() {
        Chat chat1 = createChat(1L, "Chat One");
        assertThrows(SQLException.class, () -> chatRepository.addChat(chat1)); // Проверяем количество чатов
    }

}
