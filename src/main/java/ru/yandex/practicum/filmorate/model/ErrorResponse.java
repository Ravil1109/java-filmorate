package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class ErrorResponse {
    // название ошибки
    final private String error;
    // подробное описание
    final private String description;

    // геттеры необходимы, чтобы Spring Boot мог получить значения полей
    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}