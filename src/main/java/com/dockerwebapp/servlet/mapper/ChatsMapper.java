package com.dockerwebapp.servlet.mapper;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.servlet.dto.ChatDto;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface UserChatsMapper {
    UserChatsMapper INSTANCE = Mappers.getMapper(UserChatsMapper.class);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")

    ChatDto convert(Chat chat);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")

    Chat convert(ChatDto chatDto);

}
