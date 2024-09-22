package com.dockerwebapp.repository.mapper;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserManagementMapper {

    public static User mapResultSetToUserManagement(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String about = resultSet.getString("about");
        String password = resultSet.getString("password");

        // Получаем сообщения и чаты из JSO
       // String chatsJson = resultSet.getString("chats");

        // Здесь вам нужно будет преобразовать JSON в объекты (например, List<Message> или List<Chat>)
        //List<Chat> chats = parseChats(chatsJson);

        return new User.UserBuilder(id, username, password)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setAbout(about)
                //  .setChats(chats) // Убедитесь, что метод setChats существует
                .build();
    }

    public static User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String about = resultSet.getString("about");
        String password = resultSet.getString("password");
        String chatsJson = resultSet.getString("chats");

        return new User.UserBuilder(id, username, password)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setAbout(about)
                .setChats(parseChats(chatsJson))
                .build();
    }

    private static List<Chat> parseChats(String chatsJson) {
        if (chatsJson == null || chatsJson.isEmpty()) {
            return List.of();
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(chatsJson, new TypeReference<List<Chat>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
