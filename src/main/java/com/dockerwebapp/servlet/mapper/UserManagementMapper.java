package com.dockerwebapp.servlet.mapper;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.User;
import com.dockerwebapp.servlet.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserManagementMapper {

    UserManagementMapper INSTANCE = Mappers.getMapper(UserManagementMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "about", target = "about")

    UserDto convert(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "about", target = "about")
    User convert(UserDto userDto);


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