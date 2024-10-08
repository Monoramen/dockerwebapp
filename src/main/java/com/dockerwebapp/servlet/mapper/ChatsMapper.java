package com.dockerwebapp.servlet.mapper;

import com.dockerwebapp.model.Chat;

import com.dockerwebapp.model.User;
import com.dockerwebapp.servlet.dto.ChatDto;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import org.mapstruct.Mapper;


@Mapper
public interface ChatsMapper {
    ChatsMapper INSTANCE = Mappers.getMapper(ChatsMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "participantIds", target = "participantIds")
    ChatDto convert(Chat chat); // Из Chat в ChatDto

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "participantIds", target = "participantIds")
    Chat convert(ChatDto chatDto); // Из ChatDto в Chat

    List<ChatDto> convert(List<Chat> chats);



    static Long userToLong(User user) {
        return user != null ? user.getId() : null;
    }


    static User longToUser(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }

    static Long chatToLong(Chat chat) {
        return chat != null ? chat.getId() : null;
    }


    static Chat longToChat(Long id) {
        if (id == null) return null;
        Chat chat = new Chat();
        chat.setId(id);
        return chat;
    }

}
