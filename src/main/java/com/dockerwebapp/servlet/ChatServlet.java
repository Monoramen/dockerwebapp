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
    private ChatService chatService;
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
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts[2].equals("addchat")) {
                try {
                    ChatDto chatDto = objectMapper.readValue(request.getInputStream(), ChatDto.class);
                    chatService.addChat(chatDto); // Предполагается, что метод существует
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create chat: " + e.getMessage());
                }
            }
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts[2].equals("updateChat")) {
                try {
                    Long chatId = Long.valueOf(pathParts[3]);
                    ChatDto chatDto = objectMapper.readValue(request.getInputStream(), ChatDto.class);
                    System.out.println(chatDto);
                    chatService.addChat(chatDto); // Предполагается, что метод существует

                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create chat: " + e.getMessage());
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts[2].equals("deleteChat")) {
                try {
                    Long chatId = Long.valueOf(pathParts[3]);
                    chatService.deleteChat(chatId); // Предполагается, что метод существует

                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create chat: " + e.getMessage());
                }
            }
        }
    }

    private Long extractUserIdFromPath(String pathInfo) {
        // Пример извлечения userId из URL: /api/users/{userId}/chats/
        String[] parts = pathInfo.split("/");
        return Long.valueOf(parts[2]); // Предполагаем, что userId находится на третьем месте
    }
}