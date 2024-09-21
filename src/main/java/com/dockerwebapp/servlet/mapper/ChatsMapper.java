package com.dockerwebapp.servlet.mapper;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.servlet.dto.ChatDto;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
<<<<<<< HEAD
import java.util.List;
import org.mapstruct.Mapper;


@Mapper
public interface ChatsMapper {
    ChatsMapper INSTANCE = Mappers.getMapper(ChatsMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    ChatDto convert(Chat chat); // Из Chat в ChatDto

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    Chat convert(ChatDto chatDto); // Из ChatDto в Chat

    List<ChatDto> convert(List<Chat> chats); // Метод для преобразования списка
=======

public interface UserChatsMapper {
    UserChatsMapper INSTANCE = Mappers.getMapper(UserChatsMapper.class);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")

    ChatDto convert(Chat chat);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")

    Chat convert(ChatDto chatDto);

>>>>>>> bb43dcbad708fe9e7ed39364e6d6485da0e85a16
}
