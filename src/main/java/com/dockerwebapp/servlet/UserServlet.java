package com.dockerwebapp.servlet;

import com.dockerwebapp.service.UserManagementService;
import com.dockerwebapp.servlet.dto.UserDto;
import com.dockerwebapp.servlet.dto.CreateUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(
        name = "UserServlet",
        description = "Represents a User servlet for REST API transactions",
        urlPatterns = {"/api/users/*"}
)
public class UserServlet extends HttpServlet {

    private transient UserManagementService userService;
    private transient ObjectMapper objectMapper;

    private void setResponseHeaders(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    @Override
    public void init() throws ServletException {
        this.userService = (UserManagementService) getServletContext().getAttribute("userService");
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        setResponseHeaders(response);
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
            return;
        }
        try {
            Long userId = getUserIdFromUrl(pathInfo);
            UserDto userDto =  userService.getById(userId);
            if (userDto != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(response.getOutputStream(), userDto);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to get user");
        }
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        setResponseHeaders(response);
        if (request.getContentLength() == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body is empty");
            return;
        }
        try {

            CreateUserDto CreateUserDto = objectMapper.readValue(request.getInputStream(), CreateUserDto.class);
            userService.createUser(CreateUserDto);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create user");
        }
    }


    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        setResponseHeaders(response);
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
            return;
        }
            Long userId = getUserIdFromUrl(pathInfo);
        try {
            UserDto userDto = objectMapper.readValue(request.getInputStream(), UserDto.class);
            userDto.setId(userId);
            userService.updateUser(userDto);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update user");
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        String pathInfo = request.getPathInfo();
        setResponseHeaders(response);
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID or username is required");
            return;
        }
        String username = pathInfo.split("/")[1];
        try {
            userService.deleteUser(username);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete user: " + e.getMessage());
        }
    }

    private Long getUserIdFromUrl(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return Long.valueOf(parts[1]);
    }

}