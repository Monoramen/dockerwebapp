package com.dockerwebapp.servlet.mapper;

import com.dockerwebapp.model.User;
import com.dockerwebapp.servlet.dto.CreateUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateUserMapper {
    CreateUserMapper INSTANCE = Mappers.getMapper(CreateUserMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User convert(CreateUserDto CreateUserDto);

}

