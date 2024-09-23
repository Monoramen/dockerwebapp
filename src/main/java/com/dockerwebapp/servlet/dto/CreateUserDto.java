package com.dockerwebapp.servlet.dto;

import java.util.Objects;

public class CreateUserDto {
    private String username;
    private String password;

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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Сравнение ссылок
        if (!(o instanceof CreateUserDto)) return false; // Проверка типа
        CreateUserDto that = (CreateUserDto) o; // Приведение типа
        return Objects.equals(username, that.username) && // Сравнение полей
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password); // Генерация хэш-кода на основе полей
    }
    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


}