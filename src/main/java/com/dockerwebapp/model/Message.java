package com.dockerwebapp.model;



import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.Objects;



public class Message {
    private Long id;
    private String text;
    private LocalDateTime dateTime;
    private User sender; // отправитель сообщения

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    private Long chatId; // чат, в котором было отправлено сообщение

    public Message(Long id, String text, User sender, Long chatId) {
        this.id = id;
        this.text = text;
        this.dateTime = LocalDateTime.now();
        this.sender = sender;
        this.chatId = chatId;
    }

    public Message() {

    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }



    // Геттеры и сеттеры
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

    // Конвертация LocalDateTime в Timestamp и наоборот
    public static Timestamp toTimestamp(LocalDateTime dateTime) {
        return dateTime == null ? null : Timestamp.valueOf(dateTime);
    }

    public static LocalDateTime fromTimestamp(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    public User getUser() {
        return sender;
    }

    public void setUser(User user) {
        this.sender = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) &&
                Objects.equals(text, message.text) &&
                Objects.equals(dateTime, message.dateTime) &&
                Objects.equals(sender, message.sender)  &&
                Objects.equals(chatId, message.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, dateTime, sender, chatId);
    }

    // Переопределение toString для удобного отображения
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", dateTime=" + dateTime +
                ", sender=" + (sender != null ? sender.getId() : "null") + // Выводим ID отправителя
                ", chatId=" + (chatId != null ? chatId : "null") + // Выводим ID чата вместо объекта
                '}';
    }
}
