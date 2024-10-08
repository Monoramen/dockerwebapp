package com.dockerwebapp.servlet.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatDto {
    private Long id;
    private String name;
    private List<UserInfoDto> participantIds;
    private List<MessageDto> messages;


    public ChatDto(Long id, String name, List<UserInfoDto> participantIds) {
        this.id = id;
        this.name = name != null ? name : "";
        this.participantIds = participantIds;

    }
    public ChatDto() {
        this.participantIds = new ArrayList<>();
        this.messages = new ArrayList<>();
    }
    public void setId(Long chatId) {
        this.id = chatId;
    }
    public Long getId() { return id;}

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<UserInfoDto> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<UserInfoDto> participantIds) {
        this.participantIds = participantIds;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatDto)) return false;
        ChatDto chatDto = (ChatDto) o;
        return Objects.equals(id, chatDto.id) &&
                Objects.equals(name, chatDto.name) &&
                Objects.equals(participantIds, chatDto.participantIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, participantIds);
    }

    @Override
    public String toString() {
        return "ChatDto{id=" + id +
                ", name='" + name +
                "', participantIds=" + participantIds + '}';
    }

}