package com.dockerwebapp.servlets;

import com.dockerwebapp.service.ChatService;
import com.dockerwebapp.servlet.ChatServlet;
import com.dockerwebapp.servlet.dto.ChatDto;
import com.dockerwebapp.servlet.dto.ChatDtoParticipant;
import com.dockerwebapp.servlet.dto.UserDto;
import com.dockerwebapp.servlet.dto.UserInfoDto;
import com.dockerwebapp.servlet.mapper.UserManagementMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ChatServletTest {
    private List<UserInfoDto> participants;
    @InjectMocks
    private ChatServlet chatServlet;

    @Mock
    private ChatService chatService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    UserManagementMapper userManagementMapper;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    ServletOutputStream outputStream;

    @Mock
    private ServletContext servletContext;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatServlet = new ChatServlet();
        chatServlet = Mockito.spy(new ChatServlet());
        participants = new ArrayList<>();
        UserInfoDto userDto = new UserInfoDto();
        userDto.setUsername("name");
        userDto.setId(1L);
        participants.add(userDto);
        when(servletContext.getAttribute("chatService")).thenReturn(chatService);
        doReturn(servletContext).when(chatServlet).getServletContext();

        try {
            chatServlet.init();

        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Test

    void testDoGet_BadRequest() throws Exception {
        when(request.getPathInfo()).thenReturn(null);
        chatServlet.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
    }


    @Test
    void testDoGet_UserChatsSuccess() throws Exception {
        when(request.getPathInfo()).thenReturn("/1/chats");

        Long userId = 1L;
        List<ChatDto> chats = new ArrayList<>();

        ChatDto chatDto = new ChatDto();
        chatDto.setId(1L);
        chatDto.setName("My Chat");

        chatDto.setParticipantIds(participants);
        chats.add(chatDto);
        when(chatService.getUserChats(userId)).thenReturn(chats);
        when(response.getOutputStream()).thenReturn(outputStream);
        objectMapper.writeValue(response.getOutputStream(), chats);

        chatServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(objectMapper).writeValue(response.getOutputStream(), chats);
    }


    @Test
    void testDoGet_SingleChatSuccess() throws Exception {
        when(request.getPathInfo()).thenReturn("/1/chat/2");

        Long chatId = 2L;
        ChatDto chatDto = new ChatDto();
        chatDto.setId(chatId);
        chatDto.setName("Chat Name");
        chatDto.setParticipantIds(participants);
        when(chatService.getChatById(chatId)).thenReturn(chatDto);

        when(response.getOutputStream()).thenReturn(outputStream);
        objectMapper.writeValue(outputStream, chatDto);

        chatServlet.doGet(request, response);
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);


        verify(objectMapper).writeValue(outputStream, chatDto);
    }

    @Test
    void testDoPostSuccess() throws Exception {
        when(request.getPathInfo()).thenReturn("/2/addchat");

        String json = "{\"id\": 1, \"name\":\"chatName\", \"participantIds\":[1,2]}";
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);

        ServletInputStream inputStream = new MockServletInputStream(jsonBytes);
        when(request.getInputStream()).thenReturn(inputStream);
        when(request.getContentLength()).thenReturn(jsonBytes.length);

        ChatDtoParticipant chatDto = new ChatDtoParticipant();
        chatDto.setId(1L);
        chatDto.setName("chatName");

        chatDto.setParticipantIds(List.of(1L,2L));

        when(objectMapper.readValue(request.getInputStream(), ChatDtoParticipant.class)).thenReturn(chatDto);

        chatServlet.doPost(request, response);

        verify(chatService).addChat(chatDto);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPostBadRequest() throws Exception {
        when(request.getPathInfo()).thenReturn("/2/addchat");
        chatServlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create chat");
    }


    @Test
    void testDoPutSuccess() throws Exception {
        when(request.getPathInfo()).thenReturn("/2/updateChat/3");

        String json = "{\"id\": 3, \"name\":\"chatName\"}";
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        ChatDto chatDto = new ChatDto();
        chatDto.setId(3L);
        chatDto.setName("chatName");

        ServletInputStream inputStream = new MockServletInputStream(jsonBytes);
        when(request.getInputStream()).thenReturn(inputStream);
        when(objectMapper.readValue(request.getInputStream(), ChatDto.class)).thenReturn(chatDto);

        chatServlet.doPut(request, response);

        verify(chatService).updateChat(chatDto);
        verify(response).setStatus(HttpServletResponse.SC_ACCEPTED);  // Проверка правильного статуса
    }

    @Test
    void testDoPutBadRequest() throws IOException {
        when(request.getPathInfo()).thenReturn("/chats/updateChat");
        ChatDto chatDto = new ChatDto();
        chatDto.setId(1L);
        when(objectMapper.readValue(any(ByteArrayInputStream.class), eq(ChatDto.class)))
                .thenReturn(chatDto);
        try {
            doThrow(new RuntimeException("Update failed")).when(chatService).updateChat(any(ChatDto.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        chatServlet.doPut(request, response);
        verify(response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), eq("Failed to create chat"));
    }

    @Test
    void testDoDeleteSuccess() throws Exception {
        when(request.getPathInfo()).thenReturn("/2/deleteChat/4");
        chatServlet.doDelete(request, response);
        verify(chatService).deleteChat(4L);
        verify(response).setStatus(HttpServletResponse.SC_ACCEPTED);
    }
    @Test
    void testDoDeleteSQLException() throws Exception {
        when(request.getPathInfo()).thenReturn("/2/deleteChat/6"); // Обратите внимание на формат пути
        doThrow(new SQLException("Database error")).when(chatService).deleteChat(6L);

        chatServlet.doDelete(request, response);

        verify(chatService).deleteChat(6L);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to delete chat");
    }

}
