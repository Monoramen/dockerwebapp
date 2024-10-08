package com.dockerwebapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chat {

    private Long id;
    private String name;
    private List<User> participantIds;
    private List<Message> messages;

    private Chat(ChatBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.participantIds = builder.participantIds;
        this.messages = builder.messages;
    }

    public Chat() {
        this.participantIds = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public Chat(Long id, String name) {
        this.id = id;
        this.name = name;
        this.participantIds = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public void setParticipantIds(List<User> participantIds) {
        this.participantIds = participantIds;
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

    public List<User> getParticipantIds() {
        return participantIds;}

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public static class ChatBuilder {
        private Long id;
        private String name;
        private List<User> participantIds = new ArrayList<>();
        private List<Message> messages = new ArrayList<>();

        public ChatBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ChatBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Chat build() {
            return new Chat(this);
        }

        public List<Message> getMessages() {
            return messages;
        }

        public List<User> setParticipants(List<User> users){
            for (User user : users) {
                participantIds.add(user);
            }
            return participantIds;
        }
    }
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Chat)) return false;
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id) &&
                Objects.equals(name, chat.name) &&
                    Objects.equals(participantIds, chat.participantIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, participantIds);
    }


    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}