package com.dockerwebapp.servlets;

import com.dockerwebapp.service.MessageService;
import com.dockerwebapp.servlet.MessageServlet;

import com.dockerwebapp.servlet.dto.MessageDto;
import com.dockerwebapp.servlet.mapper.MessageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class MessageServletTest {
    @InjectMocks
    private MessageServlet messageServlet;

    @Mock
    private MessageService messageService;

    @Mock
    private MessageMapper messageMapper;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    ServletOutputStream outputStream;

    @Mock
    private ServletContext servletContext;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        messageServlet = new MessageServlet();
        messageServlet = Mockito.spy(new MessageServlet());
        when(servletContext.getAttribute("messageService")).thenReturn(messageService);
        doReturn(servletContext).when(messageServlet).getServletContext();
        try {
            messageServlet.init();

        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testDoGet() throws Exception {
        when(request.getPathInfo()).thenReturn("/chat/1");
        MessageDto messageDto = new MessageDto();
        messageDto.setId(1L);
        messageDto.setChatId(1L);
        messageDto.setText("Hello!");
        messageDto.setSenderId(1L);
        messageDto.setDateTime("2022-01-01 00:00:00");
        messageDto.setChatId(1L);
        List<MessageDto> messagesDto = new ArrayList<>();
        messagesDto.add(messageDto);
        when(messageService.findAll(1L)).thenReturn(messagesDto);
        when(response.getOutputStream()).thenReturn(outputStream);
        objectMapper.writeValue(response.getOutputStream(), messagesDto);
        messageServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(objectMapper).writeValue(response.getOutputStream(), messagesDto);
    }


    @Test
    void testDoGetServiceException() throws Exception {
        // Мокаем путь запроса
        when(request.getPathInfo()).thenReturn("/chat/1");
        when(messageService.findAll(1L)).thenThrow(new RuntimeException("Database error"));
        messageServlet.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to get messages");
    }

    @Test
    void testDoPostSuccess() throws Exception {
        when(request.getPathInfo()).thenReturn("/chat/1");

        String json = "{\"id\":1, \"text\":\"Hello!\",\"dateTime\":\"2022-01-01 00:00:00\", \"senderId\":1, \"chatId\":1}";
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);

        ServletInputStream inputStream = new MockServletInputStream(jsonBytes);
        when(request.getInputStream()).thenReturn(inputStream);
        when(request.getContentLength()).thenReturn(jsonBytes.length);

        MessageDto messageDto = new MessageDto();
        messageDto.setId(1L);
        messageDto.setText("Hello!");
        messageDto.setDateTime("2022-01-01 00:00:00");
        messageDto.setSenderId(1L);
        messageDto.setChatId(1L);
        when(objectMapper.readValue(request.getInputStream(), MessageDto.class)).thenReturn(messageDto);
        messageServlet.doPost(request, response);
        verify(messageService).save(messageDto);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPostServiceException() throws Exception {
        when(request.getPathInfo()).thenReturn("/chat/1");
        messageServlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create message");
    }

    @Test
    void testDoPutSuccess() throws Exception {
        when(request.getPathInfo()).thenReturn("/update");
        String json = "{\"id\":1, \"text\":\"Updated message\", \"dateTime\":\"2022-01-02 00:00:00\", \"senderId\":1, \"chatId\":1}";
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        MessageDto messageDto = new MessageDto();
        messageDto.setId(1L);
        messageDto.setText("Updated message");
        messageDto.setDateTime("2022-01-02 00:00:00");
        messageDto.setSenderId(1L);
        messageDto.setChatId(1L);
        ServletInputStream inputStream = new MockServletInputStream(jsonBytes);
        when(request.getInputStream()).thenReturn(inputStream);
        when(request.getContentLength()).thenReturn(jsonBytes.length);
        when(objectMapper.readValue(request.getInputStream(), MessageDto.class)).thenReturn(messageDto);
        messageServlet.doPut(request, response);
        verify(messageService).update(messageDto);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPutServiceException() throws Exception {
        when(request.getPathInfo()).thenReturn(null);
        messageServlet.doPut(request, response);
        verify(response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), eq("Path information is required."));
    }

    @Test
    void testDoPutValidPathBadRequest() throws IOException {
        when(request.getPathInfo()).thenReturn("/update");
        MessageDto messageDto = new MessageDto();
        when(objectMapper.readValue(any(ByteArrayInputStream.class), eq(MessageDto.class)))
                .thenReturn(messageDto);
        doThrow(new RuntimeException("Update failed")).when(messageService).update(any(MessageDto.class));
        messageServlet.doPut(request, response);
        verify(response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), eq("Failed to create message"));
    }

    @Test
    void testDoDeleteSuccess() throws Exception {
        when(request.getPathInfo()).thenReturn("/delete/1");
        Long messageId = 1L;
        messageServlet.doDelete(request, response);
        verify(messageService).delete(messageId);
        verify(response).setStatus(HttpServletResponse.SC_ACCEPTED);
    }
    @Test
    void testDoDeleteInvalidPath() throws Exception {
        when(request.getPathInfo()).thenReturn("/invalid");
        messageServlet.doDelete(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path.");
    }

}
