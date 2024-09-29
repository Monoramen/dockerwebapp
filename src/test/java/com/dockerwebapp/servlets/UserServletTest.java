package com.dockerwebapp.servlets;

import com.dockerwebapp.model.User;
import com.dockerwebapp.service.UserManagementService;
import com.dockerwebapp.servlet.UserServlet;
import com.dockerwebapp.servlet.dto.CreateUserDto;
import com.dockerwebapp.servlet.dto.UserDto;
import com.dockerwebapp.servlet.mapper.UserManagementMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.mockito.Mockito.*;


class UserServletTest {
    @InjectMocks
    private UserServlet userServlet;

    @Mock
    private UserManagementService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    UserManagementMapper userManagementMapper;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    ServletOutputStream outputStream;

    @Mock
    private ServletContext servletContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userServlet = new UserServlet();
        userServlet = Mockito.spy(new UserServlet());
        Mockito.when(servletContext.getAttribute("userService")).thenReturn(userService);
        doReturn(servletContext).when(userServlet).getServletContext();

        try {
            userServlet.init();

        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testDoGet_BadRequest() throws Exception {
        when(request.getPathInfo()).thenReturn(null);
        userServlet.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
    }



    @Test
    void testDoGet_UserNotFound() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");
        when(userService.getById(1L)).thenReturn(null); // Мокируем ответ сервиса
        userServlet.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
    }


    @Test
    void testDoGet() throws Exception {
        // Мокируем PathInfo для запроса
        when(request.getPathInfo()).thenReturn("/1");

        Long id = 1L;
        String username = "John Doe";
        String firstName = "John";
        String lastName = "Doe";
        String about = "About John Doe";
        String password = "password123";

        User user = new User.UserBuilder(id, username, password)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setAbout(about)
                .setId(id)
                .build();

        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setUsername(username);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setAbout(about);
        userDto.setPassword(password);

        when(userManagementMapper.convert(user)).thenReturn(userDto);
        when(userService.getById(1L)).thenReturn(userDto);
        when(response.getOutputStream()).thenReturn(outputStream);
        objectMapper.writeValue(response.getOutputStream(), userDto);

        userServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(objectMapper).writeValue(response.getOutputStream(), userDto);
    }

    @Test
    void testDoPost_BadRequest() throws Exception {
        when(request.getInputStream()).thenReturn(null);
        userServlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body is empty");
    }

    @Test
    void testDoPost_BadRequest_EmptyBody() throws Exception {
        when(request.getContentLength()).thenReturn(0); // Simulating empty body
        userServlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body is empty");
    }



    @Test
    void testDoPostSuccess() throws Exception {
        String json = "{\"username\":\"newuser\", \"password\":\"password\"}";
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);

        ServletInputStream inputStream = new MockServletInputStream(jsonBytes);
        when(request.getInputStream()).thenReturn(inputStream);
        when(request.getContentLength()).thenReturn(jsonBytes.length);

        CreateUserDto expectedCreateUserDto = new CreateUserDto();
        expectedCreateUserDto.setUsername("newuser");
        expectedCreateUserDto.setPassword("password");

        when(objectMapper.readValue(request.getInputStream(), CreateUserDto.class)).thenReturn(expectedCreateUserDto);

        userServlet.doPost(request, response);

        verify(userService).createUser(expectedCreateUserDto);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPut_BadRequest() throws Exception {
        when(request.getPathInfo()).thenReturn(null);
        userServlet.doPut(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
    }

    @Test
    void testDoPutSQLException() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");
        String json = "{\"username\":\"John Doe\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"about\":\"About John Doe\",\"password\":\"password123\"}";
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        when(request.getInputStream()).thenReturn(new MockServletInputStream(jsonBytes));
        doThrow(new SQLException("Database error while updating user")).when(userService).updateUser(any(UserDto.class));
        userServlet.doPut(request, response);
        verify(userService).updateUser(any(UserDto.class)); // Verify that updateUser was called
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update user");
    }

    @Test
    void testDoPutSuccess() throws Exception {
        // Мокаем pathInfo для получения ID из URL
        when(request.getPathInfo()).thenReturn("/1");
        String json = "{\"username\":\"John Doe\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"about\":\"About John Doe\",\"password\":\"password123\"}";
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);

        Long id = 1L;
        String username = "John Doe";
        String firstName = "John";
        String lastName = "Doe";
        String about = "About John Doe";
        String password = "password123";

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setAbout(about);
        userDto.setPassword(password);
        userDto.setId(id);


        ServletInputStream inputStream = new MockServletInputStream(jsonBytes);
        when(request.getInputStream()).thenReturn(inputStream);
        when(objectMapper.readValue(request.getInputStream(), UserDto.class)).thenReturn(userDto);

        userServlet.doPut(request, response);
        verify(userService).updateUser(userDto);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoDeleteSuccess() throws Exception {
        when(request.getPathInfo()).thenReturn("/john_doe");
        userServlet.doDelete(request, response);
        verify(userService).deleteUser("john_doe");
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoDeleteBadRequest() throws Exception {
        when(request.getPathInfo()).thenReturn("/");
        userServlet.doDelete(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID or username is required");
    }

    @Test
    void testDoDeleteSQLException() throws Exception {
        when(request.getPathInfo()).thenReturn("/john_doe");
        doThrow(new SQLException("Database error")).when(userService).deleteUser("john_doe");
        userServlet.doDelete(request, response);
        verify(userService).deleteUser("john_doe");
        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete user: Database error");
    }



    @Test
    void testDoGetUserIdIsRequired() throws Exception {
        when(request.getPathInfo()).thenReturn(null);  // Симуляция отсутствующего ID

        userServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
    }

    @Test
    void testDoGetUserNotFound() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");
        when(userService.getById(1L)).thenReturn(null);  // Симуляция отсутствующего пользователя

        userServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
    }

    @Test
    void testDoGetSQLException() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");
        when(userService.getById(1L)).thenThrow(new SQLException("Database error"));  // Симуляция SQLException
        userServlet.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to get user");
    }





}
