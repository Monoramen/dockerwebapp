package com.dockerwebapp.servlet;


import com.dockerwebapp.service.MessageService;
import com.dockerwebapp.service.impl.MessageServiceImpl;
import com.dockerwebapp.servlet.dto.ChatDto;
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
        urlPatterns = {"/api/messages/*"})

public class MessageServlet extends HttpServlet {

    private MessageService messageService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.messageService = new MessageServiceImpl();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

            if (pathInfo != null) {
                String[] pathParts = pathInfo.split("/");

                if (pathParts[1].equals("chat")) {
                    try {
                        Long chatId = Long.valueOf(pathParts[2]);
                        List<MessageDto> messages = messageService.findAll(chatId);
                        String jsonResponse = objectMapper.writeValueAsString(messages);
                        response.getWriter().write(jsonResponse);
                        response.setStatus(HttpServletResponse.SC_OK); //
                    } catch (Exception e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to get messages: " + e.getMessage());
                    }
                }
            }

        }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts.length == 3 && pathParts[1].equals("chat")) { // Проверяем длину массива
                try {
                    Long chatId = Long.valueOf(pathParts[2]);
                    MessageDto messageDto = objectMapper.readValue(request.getReader(), MessageDto.class);
                    // Устанавливаем chatId из пути
                    messageDto.setChatId(chatId);
                    messageService.save(messageDto); // Сохраняем сообщение
                    response.setStatus(HttpServletResponse.SC_CREATED); // Успешное создание сообщения
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create message: " + e.getMessage());
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Path information is required.");
        }
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts[1].equals("update")) { // Проверяем длину массива
                try {
                    MessageDto messageDto = objectMapper.readValue(request.getReader(), MessageDto.class);
                    messageService.update(messageDto); // Сохраняем сообщение
                    response.setStatus(HttpServletResponse.SC_CREATED); // Успешное создание сообщения
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create message: " + e.getMessage());
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Path information is required.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts[1].equals("delete")) { // Проверяем длину массива
                try {
                    Long messageId = Long.valueOf(pathParts[2]);
                    messageService.delete(messageId); // Сохраняем сообщение
                    response.setStatus(HttpServletResponse.SC_CREATED); // Успешное создание сообщения
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create message: " + e.getMessage());
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Path information is required.");
        }
    }


    }



