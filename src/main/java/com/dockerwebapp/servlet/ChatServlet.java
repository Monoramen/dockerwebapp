package com.dockerwebapp.servlet;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.service.ChatService;
import com.dockerwebapp.service.impl.ChatServiceImpl;
import com.dockerwebapp.servlet.dto.ChatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(
        name = "ChatServlet",
        description = "Represents a Chat servlet for REST API transactions",
        urlPatterns = {"/api/user/*"},
        initParams={
        @WebInitParam(name = "user", value = "chats")}
)
public class ChatServlet extends HttpServlet {
    private ChatService chatService;  // Используем интерфейс для большей гибкости
    private ObjectMapper objectMapper;  // Для работы с JSON

    @Override
    public void init() throws ServletException {
        chatService = new ChatServiceImpl(); // Инициализация сервиса
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Для поддержки Java 8 Date/Time API
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Long userId;
        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

                if (pathParts.length == 3 && pathParts[2].equals("chats")) {
                    userId = Long.valueOf(pathParts[1]);
                    List<ChatDto> chats;
                    try {
                        chats = chatService.getUserChats(userId);
                        System.out.println(chats);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_OK);
                        objectMapper.writeValue(response.getOutputStream(), chats);
                }
            if (pathParts.length == 4 && pathParts[2].equals("chat")) { // Изменено на index 3 для "chat"
                Long chatId = Long.valueOf(pathParts[3]); // Предполагается, что chatId находится на index 4
                Chat chat;
                chat = chatService.getChatById(chatId);
                System.out.println(chat);
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(response.getOutputStream(), chat);
            }

        }    else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ChatDto chatDto = objectMapper.readValue(request.getInputStream(), ChatDto.class);

        try {
            chatService.addChat(chatDto); // Предполагается, что метод существует
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create chat: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long chatId = Long.valueOf(request.getPathInfo().substring(1));
        ChatDto chatDto = objectMapper.readValue(request.getInputStream(), ChatDto.class);

        try {
            chatDto.setId(chatId); // Устанавливаем ID чата из URL
            chatService.updateChat(chatDto); // Предполагается, что метод существует
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update chat: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Извлекаем ID чата из URL
        Long chatId = Long.valueOf(pathInfo.substring(pathInfo.lastIndexOf('/') + 1));

        // Создаем ChatDto с ID чата
        //ChatDto chatDto = new ChatDto();
        //chatDto.setId(chatId);

        // Вызываем метод удаления чата в сервисе
        //chatService.deleteChat(chatDto);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT); // Успешное удаление
    }

    private Long extractUserIdFromPath(String pathInfo) {
        // Пример извлечения userId из URL: /api/users/{userId}/chats/
        String[] parts = pathInfo.split("/");
        return Long.valueOf(parts[2]); // Предполагаем, что userId находится на третьем месте
    }
}