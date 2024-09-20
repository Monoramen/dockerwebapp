package com.dockerwebapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String about;
    private String password;
    private List<Chat> chats = new ArrayList<>();

    // Конструктор по умолчанию
    public User() {}

    // Конструктор с использованием Builder
    private User(UserBuilder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.about = builder.about;
        this.password = builder.password;
        this.chats = builder.chats;
    }

    public static class UserBuilder {
        private Long id;
        private String username;
        private String firstName = "";
        private String lastName = "";
        private String about = "";
        private String password;
        private List<Chat> chats = new ArrayList<>();

        public UserBuilder(Long id, String username, String password) {
            this.id = id;
            this.username = username;
            this.password = password;
        }

        public UserBuilder setUsername(String username) {
            this.username = username;
            return this; // Возвращаем текущий объект для поддержки цепочки вызовов
        }

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setAbout(String about) {
            this.about = about;
            return this;
        }



        public UserBuilder setChats(List<Chat> chats) {
            this.chats = chats != null ? new ArrayList<>(chats) : new ArrayList<>();
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) { // Добавлен сеттер для id
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Chat> getChats() {
        return new ArrayList<>(chats); // Возвращаем копию списка
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(about, user.about);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", about='" + about + '\'' +
                '}';
    }
}