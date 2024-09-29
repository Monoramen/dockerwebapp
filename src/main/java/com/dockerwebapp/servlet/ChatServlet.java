package com.dockerwebapp.servlet;

import com.dockerwebapp.service.ChatService;

import com.dockerwebapp.servlet.dto.ChatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
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
        urlPatterns = {"/api/user/*"})
public class ChatServlet extends HttpServlet {

    private transient ChatService chatService;
    private transient ObjectMapper objectMapper;  // Для работы с JSON

    @Override
    public void init() throws ServletException {
        this.chatService = (ChatService) getServletContext().getAttribute("chatService");
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
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
                        throw new IllegalArgumentException(e);
                    }
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_OK);
                        objectMapper.writeValue(response.getOutputStream(), chats);
                }
            if (pathParts.length == 4 && pathParts[2].equals("chat")) {
                Long chatId = Long.valueOf(pathParts[3]);
                ChatDto chat;
                chat = chatService.getChatById(chatId);
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(response.getOutputStream(), chat);
            }

        }    else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts[2].equals("addchat")) {
                try {
                    ChatDto chatDto = objectMapper.readValue(request.getInputStream(), ChatDto.class);
                    chatService.addChat(chatDto); // Предполагается, что метод существует
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create chat");
                }
            }
        }

    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts[2].equals("updateChat")) {
                try {
                    ChatDto chatDto = objectMapper.readValue(request.getInputStream(), ChatDto.class);
                    //chatService.addChat(chatDto);
                    Long chatId = chatDto.getId();
                    chatDto.setId(chatId);
                    chatService.updateChat(chatDto);
                    response.setStatus(HttpServletResponse.SC_ACCEPTED);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create chat");
                }
            }
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts[2].equals("deleteChat")) {
                try {
                    Long chatId = Long.valueOf(pathParts[3]);
                    chatService.deleteChat(chatId);
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_ACCEPTED);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to delete chat");
                }
            }
        }
    }

}