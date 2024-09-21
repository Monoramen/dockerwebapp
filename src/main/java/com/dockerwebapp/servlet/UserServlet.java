package com.dockerwebapp.servlet;


import com.dockerwebapp.service.impl.UserManagementServiceImpl;
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
    private UserManagementServiceImpl userService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.userService =  new UserManagementServiceImpl();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Service method called");
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
            return;
        }
        try {
            Long userId = getUserIdFromUrl(pathInfo);
            UserDto userDto = (UserDto) userService.getById(userId); // Предполагается, что метод существует
            if (userDto != null) {
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getOutputStream(), userDto);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Received POST request for adding user");

        // Убедитесь, что тело запроса не пустое
        if (req.getContentLength() == 0) {
            System.out.println("Request body is empty");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body is empty");
            return;
        }

        try {
            CreateUserDto CreateUserDto = objectMapper.readValue(req.getInputStream(), CreateUserDto.class);
            userService.createUser(CreateUserDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            System.out.println("User created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create user: " + e.getMessage());
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("Received POST request for update user");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
            return;
        }
        Long userId = getUserIdFromUrl(pathInfo);
        System.out.println("User ID: " + req.getInputStream());
        try {
            UserDto userDto = objectMapper.readValue(req.getInputStream(), UserDto.class);

            userDto.setId(userId); // Устанавливаем ID пользователя из URL
            userService.updateUser(userDto); // Предполагается, что метод существует
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update user: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID or username is required");
            return;
        }
        String username = pathInfo.split("/")[1];

        try {
            userService.deleteUser(username); // Предполагается, что метод существует и принимает имя пользователя
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // Успешное удаление
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete user: " + e.getMessage());
        }
    }

    private Long getUserIdFromUrl(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return Long.valueOf(parts[1]);
    }
}