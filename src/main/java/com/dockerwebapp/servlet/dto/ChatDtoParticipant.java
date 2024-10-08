package com.dockerwebapp.servlet.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatDtoParticipant {
    private Long id;
    private String name;
    private List<Long> participantIds;



    public ChatDtoParticipant(Long id, String name, List<Long> participantIds) {
        this.id = id;
        this.name = name != null ? name : "";
        this.participantIds = participantIds;

    }

    public ChatDtoParticipant() {
        this.participantIds = new ArrayList<>();
    }

    public void setId(Long chatId) {
        this.id = chatId;
    }

    public Long getId() { return id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name; }


    public List<Long> getParticipantIds() {
        return participantIds; }

    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatDtoParticipant)) return false;
        ChatDtoParticipant chatDto = (ChatDtoParticipant) o;
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
        return "ChatDto{id=" + id + ", name='" + name + '\'' + ", participantIds=" + participantIds + '}';
    }

}