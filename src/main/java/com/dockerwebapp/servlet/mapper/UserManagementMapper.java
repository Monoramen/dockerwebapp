package com.dockerwebapp.servlet.mapper;

import com.dockerwebapp.model.User;
import com.dockerwebapp.servlet.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserManagementMapper {

    UserManagementMapper INSTANCE = Mappers.getMapper(UserManagementMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "about", target = "about")
    @Mapping(source = "chats", target = "chats")
    UserDto convert(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "about", target = "about")
    @Mapping(source = "chats", target = "chats")
    User convert(UserDto userDto);

}