package com.dockerwebapp.service;

import com.dockerwebapp.servlet.dto.MessageDto;


import java.util.List;

public interface MessageService {

    MessageDto findById(Long id);
    MessageDto save(MessageDto messageDto);
    void update(MessageDto messageDto);
    void delete(Long id);
    List<MessageDto> findAll();
    List<MessageDto> findByChatId(Long chatId);
}
