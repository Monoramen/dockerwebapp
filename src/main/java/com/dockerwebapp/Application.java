package com.dockerwebapp;

import com.dockerwebapp.service.ChatService;
import com.dockerwebapp.service.MessageService;
import com.dockerwebapp.service.UserManagementService;
import com.dockerwebapp.service.impl.ChatServiceImpl;
import com.dockerwebapp.service.impl.MessageServiceImpl;
import com.dockerwebapp.service.impl.UserManagementServiceImpl;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class Application implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UserManagementService userService = new UserManagementServiceImpl();
        sce.getServletContext().setAttribute("userService", userService);

        ChatService chatService = new ChatServiceImpl();
        sce.getServletContext().setAttribute("chatService", chatService);

        MessageService messageService = new MessageServiceImpl();
        sce.getServletContext().setAttribute("messageService", messageService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Не используется
    }
}
