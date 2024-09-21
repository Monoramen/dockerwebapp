package com.dockerwebapp.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private Long id;
    private String name;
    private List<User> participants; // участники чата
    private List<Message> messages; // сообщения чата

    private Chat(ChatBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.participants = builder.participants;
        this.messages = builder.messages; // Устанавливаем сообщения из билдера
    }

    public Chat() {
        this.participants = new ArrayList<>();
        this.messages = new ArrayList<>(); // Инициализируем список сообщений
    }

    public Chat(Long id, String name) {
        this.id = id;
        this.name = name;
        this.participants = new ArrayList<>();
        this.messages = new ArrayList<>(); // Инициализируем список сообщений
    }

    public void addParticipant(User user) {
        if (user != null && !this.participants.contains(user)) {
            this.participants.add(user);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    // Метод для получения сообщений
    public List<Message> getMessages() {
        return messages; // Возвращаем список сообщений
    }

    // Метод для добавления сообщения
    public void addMessage(Message message) {
        if (message != null) {
            messages.add(message); // Добавляем сообщение в список
        }
    }

    // Метод для установки списка сообщений
    public void setMessages(List<Message> messages) {
        this.messages = messages; // Устанавливаем новый список сообщений
    }

    // Статический класс Builder
    public static class ChatBuilder {
        private Long id;
        private String name;
        private List<User> participants = new ArrayList<>();
        private List<Message> messages = new ArrayList<>(); // Инициализируем список сообщений

        public ChatBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ChatBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ChatBuilder setParticipants(List<User> participants) {
            this.participants = participants; // Устанавливаем участников
            return this;
        }

        public ChatBuilder setMessages(List<Message> messages) {
            this.messages = messages; // Устанавливаем сообщения
            return this;
        }

        // Метод для создания объекта Chat
        public Chat build() {
            return new Chat(this);
        }

        // Метод для получения списка сообщений
        public List<Message> getMessages() {
            return messages;
        }
    }
    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", participantsCount=" + participants.size() + // Количество участников
                ", messagesCount=" + messages.size() + // Количество сообщений
                '}';
    }
}