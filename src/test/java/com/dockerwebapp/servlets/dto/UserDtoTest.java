package com.dockerwebapp.servlets.dto;

import com.dockerwebapp.servlet.dto.CreateUserDto;
import com.dockerwebapp.servlet.dto.UserDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserDtoTest {

    @Test
    void testUserDtoToString() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setAbout("about");
        user.setChats(null);
        assertEquals("{id=1, username='username', firstName='firstName', lastName='lastName', about='about', password='password', chats=null}", user.toString());
    }

    @Test
    void testUserDtoHashCode() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setAbout("about");
        user.setChats(null);
        UserDto user2 = new UserDto();
        user2.setId(1L);
        user2.setUsername("username");
        user2.setPassword("password");
        user2.setFirstName("firstName");
        user2.setLastName("lastName");
        user2.setAbout("about");
        user2.setChats(null);
        assertEquals(user2.hashCode(), user.hashCode());
    }

    @Test
    void testNotEqualsHashCode() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setAbout("about");
        user.setChats(null);
        UserDto user2 = new UserDto();
        user2.setId(1L);
        user2.setUsername("user");
        user2.setPassword("password");
        user2.setFirstName("firstName");
        user2.setLastName("lastName");
        user2.setAbout("about");
        user2.setChats(null);
        assertNotEquals(user2.hashCode(), user.hashCode());
    }


    @Test
    void testCreateUserDto() {
        CreateUserDto user = new CreateUserDto();
        user.setUsername("username");
        user.setPassword("password");
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
    }

    @Test
    void testEquals() {
        CreateUserDto user = new CreateUserDto();
        user.setUsername("username");
        user.setPassword("password");
        CreateUserDto user2 = new CreateUserDto();
        user2.setUsername("username");
        user2.setPassword("password");
        assertEquals(user, user2);
    }

    @Test
    void testFalseEquals() {
        CreateUserDto user = new CreateUserDto();
        user.setUsername("username");
        user.setPassword("password");
        CreateUserDto user2 = new CreateUserDto();
        user2.setUsername("user");
        user2.setPassword("password");
        assertNotEquals(user, user2);
    }

    @Test
    void testHashCode() {
        CreateUserDto user = new CreateUserDto();
        user.setUsername("username");
        user.setPassword("password");
        CreateUserDto user2 = new CreateUserDto();
        user2.setUsername("username");
        user2.setPassword("password");
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testFalseHashCode() {
        CreateUserDto user = new CreateUserDto();
        user.setUsername("username");
        user.setPassword("password");
        CreateUserDto user2 = new CreateUserDto();
        user2.setUsername("username2");
        user2.setPassword("password2");
        assertNotEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        CreateUserDto user = new CreateUserDto();
        user.setUsername("username");
        user.setPassword("password");
        assertEquals("CreateUserDto{username='username', password='password'}", user.toString());
    }


}
