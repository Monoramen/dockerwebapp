package com.dockerwebapp.servlet;


import com.dockerwebapp.service.MessageService;
import com.dockerwebapp.service.impl.MessageServiceImpl;
import com.dockerwebapp.servlet.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@WebServlet(
        name = "MessageServlet",
        description = "Represents a Messages servlet for REST API transactions",
        urlPatterns = {"/api/users/*/chats/*/messages/*"})

public class MessageServlet extends HttpServlet {

    private MessageService messageService;  // Используем интерфейс для большей гибкости
    private ObjectMapper objectMapper;  // Для работы с JSON

    @Override
    public void init() throws ServletException {
        this.messageService = new MessageServiceImpl();  // Передаем репозиторий в сервис
        this.objectMapper = new ObjectMapper();  // Инициализация для работы с JSON
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (pathInfo == null || pathInfo.equals("/")) {
            // Получение всех сообщений
            List<MessageDto> messages = messageService.findAll();

            String jsonResponse = objectMapper.writeValueAsString(messages);
            resp.getWriter().write(jsonResponse);
            System.out.println("Servlet: json = " + jsonResponse);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            Long id = Long.parseLong(req.getPathInfo().split("/")[1]);
            MessageDto message = messageService.findById(id);
            if (message != null) {
                    String jsonResponse = objectMapper.writeValueAsString(message);
                    resp.getWriter().write(jsonResponse);
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\": \"Message not found\"}");
                }

            }
        }
    }



