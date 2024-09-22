package com.dockerwebapp.repository.impl;
import com.dockerwebapp.model.User;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WholeEmptyTableTest extends AbstractDatabaseTest {
    private UserManagementRepositoryImpl userManagementRepository;


    @BeforeEach
    void init() throws SQLException {
        initializeDatabaseConnection();
        clearDatabase();
        userManagementRepository = new UserManagementRepositoryImpl();
    }

    private User createUser(Long id, String username, String password) {
        return new User.UserBuilder(id, username, password).build();
    }
    @Test
    void testNotExistsTableCreateUser() {
        User user = createUser(1L, "john_doe", "password123");
        assertThrows(SQLException.class, () -> {
            userManagementRepository.createUser(user);
        });
    }



    @Test
    void testNotExistsTableGetById() {
        assertThrows(SQLException.class, () -> {
            userManagementRepository.getById(1L);
        });
    }
    @Test
    void testNotExistsTableGetByUsername() {
        assertThrows(SQLException.class, () -> {
            userManagementRepository.getByUsername("john_doe");
        });
    }

    @Test
    void testNotExistsTableDeleteUser() {
        assertThrows(SQLException.class, () -> {
            userManagementRepository.deleteUser("john_doe");
        });
    }


}