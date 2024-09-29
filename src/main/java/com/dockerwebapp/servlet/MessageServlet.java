package com.dockerwebapp.servlet;


import com.dockerwebapp.service.MessageService;
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

    private transient MessageService messageService;
    private transient ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.messageService = (MessageService) getServletContext().getAttribute("messageService");
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

            if (pathInfo != null) {
                String[] pathParts = pathInfo.split("/");

                if (pathParts[1].equals("chat")) {
                    try {
                        Long chatId = Long.valueOf(pathParts[2]);
                        List<MessageDto> messages = messageService.findAll(chatId);


                        objectMapper.writeValue(response.getOutputStream(), messages);
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_OK);

                    } catch (Exception e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to get messages");
                    }
                }
            }
        }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();


        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts.length == 3 && pathParts[1].equals("chat")) {
                try {
                    Long chatId = Long.valueOf(pathParts[2]);
                    MessageDto messageDto = objectMapper.readValue(request.getInputStream(), MessageDto.class);
                    messageDto.setChatId(chatId);
                    messageService.save(messageDto); // Сохраняем сообщение
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create message");
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Path information is required.");
        }
    }


    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        // Устанавливаем тип контента и кодировку в начале
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts[1].equals("update")) {
                try {
                    MessageDto messageDto = objectMapper.readValue(request.getInputStream(), MessageDto.class);
                    messageService.update(messageDto);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create message");
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Path information is required.");
        }
    }


    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        // Устанавливаем тип контента и кодировку в начале
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts[1].equals("delete")) {
                try {
                    Long messageId = Long.valueOf(pathParts[2]);
                    messageService.delete(messageId);
                    response.setStatus(HttpServletResponse.SC_ACCEPTED);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to delete message");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Path information is required.");
        }
    }



    }



