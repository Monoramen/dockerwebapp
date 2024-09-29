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


    @Override
    public void init() throws ServletException {
        this.userService = (UserManagementService) getServletContext().getAttribute("userService");
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
            return;
        }
        try {
            Long userId = getUserIdFromUrl(pathInfo);
            UserDto userDto =  userService.getById(userId);
            if (userDto != null) {
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getOutputStream(), userDto);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to get user");
        }
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {

        if (req.getContentLength() == 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body is empty");
            return;
        }
        try {
            CreateUserDto CreateUserDto = objectMapper.readValue(req.getInputStream(), CreateUserDto.class);
            userService.createUser(CreateUserDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create user");
        }
    }


    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
            return;
        }
        
            Long userId = getUserIdFromUrl(pathInfo);

        try {
            UserDto userDto = objectMapper.readValue(req.getInputStream(), UserDto.class);
            userDto.setId(userId);
            userService.updateUser(userDto);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update user");
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID or username is required");
            return;
        }
        String username = pathInfo.split("/")[1];
        try {
            userService.deleteUser(username);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete user: " + e.getMessage());
        }
    }

    private Long getUserIdFromUrl(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return Long.valueOf(parts[1]);
    }

}