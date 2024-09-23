package com.dockerwebapp.repository.servlets;

import com.dockerwebapp.db.DataBaseConnector;
import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.User;
import com.dockerwebapp.service.UserManagementService;
import com.dockerwebapp.service.impl.UserManagementServiceImpl;
import com.dockerwebapp.servlet.UserServlet;
import com.dockerwebapp.servlet.dto.ChatDto;
import com.dockerwebapp.servlet.dto.CreateUserDto;
import com.dockerwebapp.servlet.dto.UserDto;
import com.dockerwebapp.servlet.mapper.CreateUserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;



import java.io.*;


import static org.mockito.Mockito.*;



class UserServletTest {

    @InjectMocks
    private UserServlet userServlet; // Внедряем моки в этот объект

    @Mock
    private UserManagementServiceImpl userService; // Мокаем сервис
    @Mock
    private ObjectMapper objectMapper;  // Мокаем ObjectMapper
    @Mock
    private CreateUserDto createUserDto;
    @Mock
    private CreateUserMapper createUserMapper;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализируем моки
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
        objectMapper = mock(ObjectMapper.class);
        createUserDto = mock(CreateUserDto.class);

        try {
            when(response.getWriter()).thenReturn(writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void testDoGet_Success() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");
        UserDto mockedUser = new UserDto();
        mockedUser.setUsername("testUser");
        when(userService.getById(1L)).thenReturn(mockedUser);
        userServlet.doGet(request, response);
        verify(userService).getById(1L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }


    @Test
    void testDoGet_BadRequest() throws Exception {
        // Мокаем запрос с отсутствующим ID
        when(request.getPathInfo()).thenReturn(null);
        // Выполняем запрос
        userServlet.doGet(request, response);
        // Проверяем, что вернулся код 400
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
    }

    @Test
    void testDoPost_Success() throws Exception {
        // Мокаем путь запроса
        when(request.getPathInfo()).thenReturn("/");

        // Создаем объект DTO
        String username = "user";
        String password = "password";
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setUsername(username);
        createUserDto.setPassword(password);

        // Преобразуем объект в JSON
        String jsonInput = objectMapper.writeValueAsString(createUserDto);

        // Мокаем входящий поток
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonInput)));

        // Мокаем сервис
        doNothing().when(userService).createUser(any(CreateUserDto.class));

        // Выполняем запрос
        userServlet.doPost(request, response);

        // Проверяем, что метод createUser был вызван с ожидаемым объектом
        verify(userService).createUser(eq(createUserDto));

        // Мокаем ответ
        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        // Проверяем, что статус и другие параметры ответа установлены правильно
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_CREATED);

        // Проверяем, что выводится правильный JSON
        verify(out).write(anyString()); // Если нужно, можно уточнить проверку
    }







    @Test
    void testDoPost_BadRequest_EmptyBody() throws Exception {
        when(request.getContentLength()).thenReturn(0);
        userServlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body is empty");
    }

}
