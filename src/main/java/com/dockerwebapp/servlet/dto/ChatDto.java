package com.dockerwebapp.servlet.dto;

import com.dockerwebapp.model.Message;
import com.dockerwebapp.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ChatDto {

    private Long id;
    private String name; // Убираем final, чтобы можно было задавать значение
    private List<User> participants; // участники чата
    private List<Message> messages;

    // Конструктор с параметрами
    public ChatDto(Long id, String name, List<User> participants, List<Message> messages) {
        this.id = id;
        this.name = name != null ? name : ""; // Устанавливаем значение по умолчанию
        this.participants = participants != null ? List.copyOf(participants) : Collections.emptyList();
        this.messages = messages != null ? List.copyOf(messages) : Collections.emptyList();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getParticipants() {
        return Collections.unmodifiableList(participants);
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public String toString() {
        return "ChatDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", participants=" + participants +
                ", messages=" + messages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatDto)) return false;
        ChatDto chatDto = (ChatDto) o;
        return Objects.equals(id, chatDto.id) &&
                Objects.equals(name, chatDto.name) &&
                Objects.equals(participants, chatDto.participants) &&
                Objects.equals(messages, chatDto.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, participants, messages);
    }

    public void setId(Long chatId) {
        this.id = chatId;
    }
}