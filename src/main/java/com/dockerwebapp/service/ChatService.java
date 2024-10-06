package com.dockerwebapp.service;

import com.dockerwebapp.servlet.dto.ChatDto;
import com.dockerwebapp.servlet.dto.ChatDtoParticipant;

import java.sql.SQLException;
import java.util.List;

public interface ChatService {

  void addChat(ChatDtoParticipant chatDto) throws SQLException;
  void deleteChat(Long chatDto) throws SQLException;
  void updateChat(ChatDto chatDto) throws SQLException;
  ChatDto getChatById(Long chatId);
  List<ChatDto> getUserChats(Long userId) throws SQLException;

}