package com.dockerwebapp.servlet.dto;



public class MessageDto {
    private Long id;
    private String text;
    private String dateTime;  // В виде строки для удобства
    private Long senderId;
    private Long chatId;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public MessageDto(Long id, String text, String dateTime, Long senderId, Long chatId) {
        this.id = id;
        this.text = text;
        this.dateTime = dateTime;
        this.senderId = senderId;
        this.chatId = chatId;
    }

    // Конструктор без параметров (если нужен)
    public MessageDto() {}

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

    public String  getDateTime() {
        return dateTime;
    }

    public void setDateTime(String  dateTime) {
        this.dateTime = dateTime;
    }


    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", senderId='" + senderId + '\'' +
                ", chatId='" + chatId + '\'' +
                '}';
    }
}
