package com.dockerwebapp.servlet.dto;

import com.dockerwebapp.model.Chat;
import com.dockerwebapp.model.User;

import java.util.List;
import java.util.Objects;

public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String about;
    private String password;
    private List<Chat> chats;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Сравнение ссылок
        if (!(o instanceof UserDto)) return false; // Проверка типа
        UserDto userDto = (UserDto) o; // Приведение типа
        return Objects.equals(id, userDto.id) && // Сравнение полей
                Objects.equals(username, userDto.username) &&
                Objects.equals(firstName, userDto.firstName) &&
                Objects.equals(lastName, userDto.lastName) &&
                Objects.equals(about, userDto.about) &&
                Objects.equals(password, userDto.password);
        // Не сравниваем chats, если это не нужно
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstName, lastName, about, password); // Генерация хэш-кода на основе полей
    }
    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", about='" + about + '\'' +
                ", password='" + password + '\'' +
                ", chats=" + chats +
                '}';
    }

}
