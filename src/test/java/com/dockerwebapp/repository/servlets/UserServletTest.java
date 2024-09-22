package com.dockerwebapp.repository.servlets;

import com.dockerwebapp.db.DataBaseConnector;
import com.dockerwebapp.model.Chat;
import com.dockerwebapp.service.impl.UserManagementServiceImpl;
import com.dockerwebapp.servlet.UserServlet;
import com.dockerwebapp.servlet.dto.ChatDto;
import com.dockerwebapp.servlet.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static java.lang.invoke.VarHandle.AccessMode.GET;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServletTest {
    @InjectMocks
    private UserServlet userServlet;

    @Mock
    private UserManagementServiceImpl userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            userServlet.init();
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
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
}
