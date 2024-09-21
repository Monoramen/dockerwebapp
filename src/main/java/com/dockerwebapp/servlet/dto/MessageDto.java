package com.dockerwebapp.servlet.dto;



public class MessageDto {
    private Long id;
    private String text;
    private String dateTime;  // В виде строки для удобства

    // Отправитель и чат — в виде строк, а не объектов
    private String senderUsername;
    private Long chatId;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
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

    public String  getDateTime() {
        return dateTime;
    }

    public void setDateTime(String  dateTime) {
        this.dateTime = dateTime;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }


    @Override
    public String toString() {
        return "MessageDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", senderUsername='" + senderUsername + '\'' +
                ", chatName='" + chatId + '\'' +
                '}';
    }
}
