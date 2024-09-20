package com.dockerwebapp.servlet.mapper;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.servlet.dto.ChatDto;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface ChatMapper {
    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "participants", target = "participants")
    @Mapping(source = "messages", target = "messages")
    Chat convert(ChatDto chatDto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "participants", target = "participants")
    @Mapping(source = "messages", target = "messages")

    ChatDto convert(Chat chat);

}
