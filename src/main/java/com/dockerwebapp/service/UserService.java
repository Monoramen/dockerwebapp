package com.dockerwebapp.service;

import com.dockerwebapp.model.Message;
import com.dockerwebapp.servlet.dto.ChatDto;
import com.dockerwebapp.servlet.dto.MessageDto;
import com.dockerwebapp.servlet.dto.UserDto;


import java.sql.SQLException;
import java.util.List;

public interface UserService {

    List<ChatDto> getUserChats(Long userId) throws SQLException;


}
