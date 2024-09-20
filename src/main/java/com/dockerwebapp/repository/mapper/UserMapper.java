package com.dockerwebapp.repository.mapper;

import com.dockerwebapp.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    public static User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        return new User.UserBuilder(id, username, password).build();
    }


}
