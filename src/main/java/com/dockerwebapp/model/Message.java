package com.dockerwebapp.model;



import java.time.LocalDateTime;
import java.util.Objects;



public class Message {
    private Long id;
    private String text;
    private LocalDateTime dateTime;
    private User senderId;
    private Chat chatId;

    public Chat getChatId() {
        return chatId;
    }

    public void setChatId(Chat chatId) {
        this.chatId = chatId;
    }


    public Message(Long id, String text,LocalDateTime dateTime, User senderId, Chat chatId) {
        this.id = id;
        this.text = text;
        this.dateTime = dateTime;
        this.senderId = senderId;
        this.chatId = chatId;
    }

    public Message() {}

    public User getSenderId() {
        return senderId;
    }

    public void setSenderId(User senderId) {
        this.senderId = senderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) &&
                Objects.equals(text, message.text) &&
                Objects.equals(dateTime, message.dateTime) &&
                Objects.equals(senderId, message.senderId)  &&
                Objects.equals(chatId, message.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, dateTime,senderId, chatId);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", dateTime=" + dateTime +
                ", senderId=" + (senderId != null ? senderId : "null") +
                ", chatId=" + (chatId != null ? chatId : "null") +
                '}';
    }
}
