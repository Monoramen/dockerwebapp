package com.dockerwebapp.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private Long id;
    private String name;
    private List<Long> participantIds; // Список идентификаторов участников
    private List<Message> messages; // Сообщения чата

    private Chat(ChatBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.participantIds = builder.participantIds; // Устанавливаем идентификаторы участников из билдера
        this.messages = builder.messages; // Устанавливаем сообщения из билдера
    }

    public Chat() {
        this.participantIds = new ArrayList<>(); // Инициализируем список идентификаторов участников
        this.messages = new ArrayList<>(); // Инициализируем список сообщений
    }

    public Chat(Long id, String name) {
        this.id = id;
        this.name = name;
        this.participantIds = new ArrayList<>(); // Инициализируем список идентификаторов участников
        this.messages = new ArrayList<>(); // Инициализируем список сообщений
    }

    public void addParticipant(Long userId) {
        if (userId != null && !this.participantIds.contains(userId)) {
            this.participantIds.add(userId); // Добавляем ID участника в список
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

    public List<Long> getParticipantIds() {
        return participantIds; // Возвращаем список идентификаторов участников
    }

    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds; // Устанавливаем новый список идентификаторов участников
    }

    public List<Message> getMessages() {
        return messages; // Возвращаем список сообщений
    }

    public void addMessage(Message message) {
        if (message != null) {
            messages.add(message); // Добавляем сообщение в список
        }
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages; // Устанавливаем новый список сообщений
    }

    // Статический класс Builder
    public static class ChatBuilder {
        private Long id;
        private String name;
        private List<Long> participantIds = new ArrayList<>(); // Инициализируем список идентификаторов участников
        private List<Message> messages = new ArrayList<>(); // Инициализируем список сообщений

        public ChatBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ChatBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ChatBuilder setParticipantIds(List<Long> participantIds) {
            this.participantIds = participantIds; // Устанавливаем идентификаторы участников
            return this;
        }

        public ChatBuilder setMessages(List<Message> messages) {
            this.messages = messages; // Устанавливаем сообщения
            return this;
        }

        public Chat build() {
            return new Chat(this);
        }

        public List<Message> getMessages() {
            return messages;
        }

        public List<Long> getParticipantIds() {
            return participantIds;
        }
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", participantsId=" + participantIds.size()+
                ", messages=" + messages +
                '}';
    }
}