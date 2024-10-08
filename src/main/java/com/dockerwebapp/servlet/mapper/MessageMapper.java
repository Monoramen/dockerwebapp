package com.dockerwebapp.servlet.mapper;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.Message;
import com.dockerwebapp.model.User;
import com.dockerwebapp.servlet.dto.MessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "senderId", target = "senderId", qualifiedByName = "userToLong")
    @Mapping(source = "chatId", target = "chatId", qualifiedByName = "chatToLong")
    @Mapping(source = "dateTime", target = "dateTime", qualifiedByName = "localDateTimeToString")
    MessageDto convert(Message message);

    @Mapping(source = "senderId", target = "senderId", qualifiedByName = "longToUser")
    @Mapping(source = "chatId", target = "chatId", qualifiedByName = "longToChat")
    @Mapping(source = "dateTime", target = "dateTime", qualifiedByName = "stringToLocalDateTime")
    Message convert(MessageDto messageDto);

    List<MessageDto> convert(List<Message> messages);

    @Named("localDateTimeToString")
    static String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }

    @Named("stringToLocalDateTime")
    static LocalDateTime stringToLocalDateTime(String dateTime) {
        return dateTime != null ? LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }

    @Named("userToLong")
    static Long userToLong(User user) {
        return user != null ? user.getId() : null;
    }

    @Named("longToUser")
    static User longToUser(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("chatToLong")
    static Long chatToLong(Chat chat) {
        return chat != null ? chat.getId() : null;
    }

    @Named("longToChat")
    static Chat longToChat(Long id) {
        if (id == null) return null;
        Chat chat = new Chat();
        chat.setId(id);
        return chat;
    }


}

