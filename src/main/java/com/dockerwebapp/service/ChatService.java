package com.dockerwebapp.service;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.servlet.dto.ChatDto;

import java.sql.SQLException;
import java.util.List;

public interface ChatService {

  void addChat(ChatDto chatDto) throws SQLException;
  void deleteChat(Long chatDto) throws SQLException;
  void updateChat(ChatDto chatDto) throws SQLException;
  Chat getChatById(Long chatId);
  List<ChatDto> getUserChats(Long userId) throws SQLException;
}