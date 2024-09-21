package com.dockerwebapp.servlet.dto;

import com.dockerwebapp.model.Message;
import com.dockerwebapp.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ChatDto {

    private Long id;
    private String name;


    // Конструктор с параметрами
    public ChatDto(Long id, String name) {
        this.id = id;
        this.name = name != null ? name : ""; // Устанавливаем значение по умолчанию
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }



    @Override
    public String toString() {
        return "ChatDto{id=" + id + ", name='" + name + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatDto)) return false;
        ChatDto chatDto = (ChatDto) o;
        return Objects.equals(id, chatDto.id) &&
                Objects.equals(name, chatDto.name);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public void setId(Long chatId) {
        this.id = chatId;
    }
}