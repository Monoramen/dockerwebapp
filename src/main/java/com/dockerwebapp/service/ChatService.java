package com.dockerwebapp.service;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.Message;
import com.dockerwebapp.servlet.dto.ChatDto;
import com.dockerwebapp.servlet.dto.MessageDto;

import java.sql.SQLException;
import java.util.List;

public interface ChatService {

  void addChat(ChatDto chatDto) throws SQLException;
  void deleteChat(ChatDto chatDto) throws SQLException;
  List<ChatDto> getUserChats(Long userId) throws SQLException;
  void updateChat(ChatDto chatDto) throws SQLException;
  Chat getChatById(Long chatId);
}