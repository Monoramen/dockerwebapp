package com.dockerwebapp.service.impl;

import com.dockerwebapp.model.Message;

import com.dockerwebapp.repository.MessageRepository;
import com.dockerwebapp.repository.impl.MessageRepositoryImpl;
import com.dockerwebapp.service.MessageService;
import com.dockerwebapp.servlet.dto.MessageDto;
import com.dockerwebapp.servlet.mapper.MessageMapper;


import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    public MessageServiceImpl() {
        this.messageRepository = new MessageRepositoryImpl();
    }

    public MessageServiceImpl(MessageRepository messageRepository) { // Конструктор для внедрения зависимости
        this.messageRepository = messageRepository;
    }
    @Override
    public List<MessageDto> findAll(Long chatId) {
        List<Message> messages;
        try {
           messages = messageRepository.findAll(chatId);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return messages.stream()
                .map(messageMapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDto> findByChatId(Long chatId) {
        List<Message> messages;
        try {
            messages = messageRepository.findByChatId(chatId);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return messages.stream()
                .map(messageMapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public MessageDto findById(Long id) {
        Message message = null;
        try {
            message = messageRepository.findById(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return messageMapper.convert(message);  // Преобразуем сущность в DTO
    }


    @Override
    public MessageDto save(MessageDto messageDto) {
        Message message = messageMapper.convert(messageDto);  // Преобразуем DTO в сущность
        try {
            messageRepository.save(message);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return messageMapper.convert(message);  // Возвращаем сохранённое сообщение в виде DTO
    }

    @Override
    public void update(MessageDto messageDto) {
        Message message = messageMapper.convert(messageDto);  // Преобразуем DTO в сущность
        try {
            messageRepository.update(message);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            messageRepository.delete(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
