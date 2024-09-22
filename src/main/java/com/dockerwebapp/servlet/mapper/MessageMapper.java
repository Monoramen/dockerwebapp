package com.dockerwebapp.servlet.mapper;

import com.dockerwebapp.model.Message;
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

    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "chatId", target = "chatId")
    @Mapping(source = "dateTime", target = "dateTime", qualifiedByName = "localDateTimeToString")
    MessageDto convert(Message message);

    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "chatId", target = "chatId")
    @Mapping(source = "dateTime", target = "dateTime", qualifiedByName = "stringToLocalDateTime")
    Message convert(MessageDto messageDto);

    @Named("localDateTimeToString")
    static String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }

    List<MessageDto> convert(List<Message> messages);
    @Named("stringToLocalDateTime")
    static LocalDateTime stringToLocalDateTime(String dateTime) {
        return dateTime != null ? LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
}

