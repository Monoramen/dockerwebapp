package com.dockerwebapp.repository.service;

import com.dockerwebapp.model.User;
import com.dockerwebapp.repository.UserManagementRepository;
import com.dockerwebapp.service.impl.UserManagementServiceImpl;
import com.dockerwebapp.servlet.dto.CreateUserDto;
import com.dockerwebapp.servlet.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserManagementServiceImplTest {

    private UserManagementServiceImpl userManagementService;
    private UserManagementRepository userRepository;
    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserManagementRepository.class);
        userManagementService = new UserManagementServiceImpl(userRepository); // Внедряем мок через конструктор
    }

    @Test
    void testCreateUser() throws SQLException {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setUsername("newuser");
        createUserDto.setPassword("password");
        userManagementService.createUser(createUserDto);
        verify(userRepository).createUser(any(User.class));
        }


    @Test
    void testUpdateUser() throws SQLException {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("existinguser");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setAbout("I am an existing user");
        userDto.setPassword("password");
        userManagementService.updateUser(userDto);
        verify(userRepository).updateUser(any(User.class));

 }


    @Test
    void testGetByUsername() throws SQLException {

        String username = "username";
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUsername("username");
        expectedUser.setPassword("password");

        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setUsername("username");
        createUserDto.setPassword("password");
        userManagementService.createUser(createUserDto);

        when(userRepository.getByUsername(username)).thenReturn(expectedUser); // Настраиваем мок
        UserDto actualUser = userManagementService.getByUsername(username);
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }

    @Test
    void testGetById() throws SQLException {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUsername("username");
        expectedUser.setPassword("password");

        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setUsername("username");
        createUserDto.setPassword("password");
        userManagementService.createUser(createUserDto);

        when(userRepository.getById(1L)).thenReturn(expectedUser);
        UserDto actualUser = userManagementService.getById(1L);
        assertEquals(expectedUser.getId(), actualUser.getId());
    }



    @Test
    void testDeleteUser() throws SQLException {
        String username = "userToDelete";
        userManagementService.deleteUser(username);
        verify(userRepository).deleteUser(username); // Проверяем вызов метода deleteUser с правильным именем пользователя
    }
}