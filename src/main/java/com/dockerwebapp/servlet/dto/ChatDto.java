package com.dockerwebapp.servlet.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatDto {
    private Long id;
    private String name;
    private List<Long> participantIds; // Список идентификаторов участников

    public ChatDto(Long id, String name, List<Long> participantIds) {
        this.id = id;
        this.name = name != null ? name : ""; // Устанавливаем значение по умолчанию
        this.participantIds = participantIds; // Инициализируем идентификаторы участников
    }

    public ChatDto() {
        // Инициализация значений по умолчанию
        this.participantIds = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name; // Добавляем setter для имени
    }


    public List<Long> getParticipantIds() {
        return participantIds; // Геттер для идентификаторов участников
    }

    @Override
    public String toString() {
        return "ChatDto{id=" + id + ", name='" + name + '\'' + ", participantIds=" + participantIds + '}';
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

    public void setId(Long chatId) {
        this.id = chatId;
    }
}