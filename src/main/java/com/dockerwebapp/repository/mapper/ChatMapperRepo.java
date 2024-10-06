package com.dockerwebapp.repository.mapper;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.User;
import com.dockerwebapp.repository.impl.UserManagementRepositoryImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ChatMapperRepo {
    public static Chat mapResultSetToChat(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        if (resultSet.wasNull()) {
            return null;
        }


        Chat chat = new Chat.ChatBuilder()
                .setId(id)
                .setName(name)
                .build();
        List<User> participants = new ArrayList<>();

        Long participantId = resultSet.getLong("participantId");
        if (!resultSet.wasNull()) {
            participants.add(new UserManagementRepositoryImpl().getById(participantId));
        }

        chat.setParticipantIds(participants);

        return chat;
    }

}