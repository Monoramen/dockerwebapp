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
    private final String errMsg = "Path information is required.";

    private void setResponseHeaders(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

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
                        setResponseHeaders(response);
                        Long chatId = Long.valueOf(pathParts[2]);
                        List<MessageDto> messages = messageService.findAll(chatId);
                        response.setStatus(HttpServletResponse.SC_OK);
                        objectMapper.writeValue(response.getOutputStream(), messages);

                    } catch (Exception e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to get messages");
                    }
                }
            }
        }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        setResponseHeaders(response);

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            if (pathParts.length == 3 && pathParts[1].equals("chat")) {
                try {
                    Long chatId = Long.valueOf(pathParts[2]);
                    MessageDto messageDto = objectMapper.readValue(request.getInputStream(), MessageDto.class);
                    messageDto.setChatId(chatId);
                    messageService.save(messageDto); // Сохраняем сообщение

                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create message");
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, errMsg);
        }
    }


    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        setResponseHeaders(response);

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
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, errMsg);
        }
    }


    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        setResponseHeaders(response);

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
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, errMsg);
        }
    }



    }



