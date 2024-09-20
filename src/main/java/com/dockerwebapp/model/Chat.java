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
        this.messages = builder.messages;
    }

    public Chat() {
        this.participants = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public Chat(Long id, String name) {
        this.id = id;
        this.name = name;
        this.participants = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public void addParticipant(User user) {
        if (!this.participants.contains(user)) {
            this.participants.add(user);
        }
    }

    public void addMessage(Message message) {
        this.messages.add(message);
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    // Статический класс Builder
    public static class ChatBuilder {
        private Long id;
        private String name;
        private List<User> participants = new ArrayList<>();
        private List<Message> messages = new ArrayList<>();

        public ChatBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ChatBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ChatBuilder addParticipant(User user) {
            if (!this.participants.contains(user)) {
                this.participants.add(user);
            }
            return this;
        }

        public ChatBuilder addMessage(Message message) {
            this.messages.add(message);
            return this;
        }

        // Метод для создания объекта Chat
        public Chat build() {
            return new Chat(this);
        }
    }


    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id
                + ", name='" + name + '\''
                +"}";
    }
}