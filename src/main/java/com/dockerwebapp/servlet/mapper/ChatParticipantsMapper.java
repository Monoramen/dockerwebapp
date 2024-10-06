package com.dockerwebapp.servlet.mapper;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.User;
import com.dockerwebapp.servlet.dto.ChatDtoParticipant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;


@Mapper
public interface ChatParticipantsMapper {
    ChatParticipantsMapper INSTANCE = Mappers.getMapper(ChatParticipantsMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "participantIds", target = "participantIds")
    ChatDtoParticipant convert(Chat chat); // Из Chat в ChatDto

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "participantIds", target = "participantIds")
    Chat convert(ChatDtoParticipant chatDtoParticipant);

    List<ChatDtoParticipant> convert(List<Chat> chats);


    default List<Long> map(List<User> users) {
        if (users == null) {
            return null;
        }
        List<Long> ids = new ArrayList<>();
        for (User user : users) {
            ids.add(user.getId());
        }
        return ids;
    }

    default List<User> mapToUsers(List<Long> ids) {
        if (ids == null) {
            return null;
        }
        List<User> users = new ArrayList<>();
        for (Long id : ids) {
            User user = new User();
            user.setId(id); // Устанавливаем ID пользователя
            users.add(user);
        }
        return users;
    }
}
