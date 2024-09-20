package com.dockerwebapp.repository.impl;



import com.dockerwebapp.model.Chat;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryImplTest extends AbstractDatabaseTest {
    private UserRepositoryImpl userRepository;


    @BeforeEach
    void init() throws SQLException {
        initializeDatabaseConnection();
        userRepository = new UserRepositoryImpl();
    }

    @Test
    void testgetUserChats() throws SQLException {
        List<Chat> chats = userRepository.getUserChats(1L);
        assertNotNull(chats);
        assertEquals("chat1", chats.get(0).getName());
    }


}
