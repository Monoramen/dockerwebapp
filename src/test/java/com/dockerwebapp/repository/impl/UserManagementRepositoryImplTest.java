package com.dockerwebapp.repository.impl;

import com.dockerwebapp.model.User;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserManagementRepositoryImplTest  extends AbstractDatabaseTest {
    private UserManagementRepositoryImpl userManagementRepository;

    private User createUser(Long id, String username, String password) {
        return new User.UserBuilder(id, username, password).build();
    }
    @BeforeEach
    void init() throws SQLException {
        initializeDatabaseConnection();
        userManagementRepository = new UserManagementRepositoryImpl();
    }


    @DisplayName("Test createUser method")
        @Test
        void testCreateUser() throws SQLException {
            User user = createUser(1L, "john_doe", "password123");
            userManagementRepository.createUser(user);

            User actualUser = userManagementRepository.getByUsername("john_doe");
            assertNotNull(actualUser);
            assertEquals(user.getUsername(),userManagementRepository.getByUsername("john_doe").getUsername());
        }

        @DisplayName("Test create exist User method")
        @Test
        void testCreateExistUser() throws SQLException {
            userManagementRepository = new UserManagementRepositoryImpl();
            User user = createUser(1L, "test2", "password123");
            userManagementRepository.createUser(user);
            User actualUser = userManagementRepository.getByUsername("test2");
            assertNotNull(actualUser);
            assertEquals(user.getUsername(), actualUser.getUsername());
        }

        @Test
        void testUpdateUser() throws SQLException {
            userManagementRepository = new UserManagementRepositoryImpl();
            User user = new User.UserBuilder(1L, "jorjenford", "updated_password")
                    .setFirstName("John")
                    .setLastName("Doe")
                    .setAbout("Just a regular user.")
                    .build();
            userManagementRepository.updateUser(user);
            System.out.println(userManagementRepository.getByUsername("jorjenford"));
            User updatedUser = userManagementRepository.getByUsername("jorjenford");
            assertEquals("updated_password", updatedUser.getPassword());
        }

        @Test
        void testDeleteUser() throws SQLException {
            userManagementRepository = new UserManagementRepositoryImpl();
            User user = createUser(1L, "john_doe", "password123");
            userManagementRepository.createUser(user); // Сначала создаем пользователя
            userManagementRepository.deleteUser(user.getUsername());
            User deletedUser = userManagementRepository.getByUsername("john_doe");
            assertNull(deletedUser); // Пользователь должен быть удален
        }


        @Test
        void testDeleteUserWithConnections() throws SQLException {
            userManagementRepository.deleteUser("first");
            userManagementRepository.deleteUser("test1");
            User deletedUser = userManagementRepository.getByUsername("first");
            assertNull(deletedUser);
            User deletedUser2 = userManagementRepository.getByUsername("test1");
            assertNull(deletedUser2);
        }

        @Test
        void testDeleteNotExistUser() throws SQLException {
            userManagementRepository.deleteUser("first1");
            User deletedUser = userManagementRepository.getByUsername("first1");
            assertNull(deletedUser);
        }

        @Test
        void testGetByUsername() throws SQLException {
            User expectedUser = createUser(1L, "john_doe", "password123");
            userManagementRepository.createUser(expectedUser); // Создаем пользователя
            User actualUser = userManagementRepository.getByUsername("john_doe");
            assertNotNull(actualUser);
            assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        }


        @Test
        void testNotExitsGetByUsername() throws SQLException {
            User actualUser = userManagementRepository.getByUsername("first1");
            assertNull(actualUser);
        }

        @Test
        void testGetById() throws SQLException {
            User expectedUser = createUser(5L, "john_doe", "password123");
            userManagementRepository.createUser(expectedUser);
            User actualUser = userManagementRepository.getById(expectedUser.getId());
            assertNotNull(actualUser);
            assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        }


}

