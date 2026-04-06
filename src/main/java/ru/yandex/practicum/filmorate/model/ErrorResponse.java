package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class ErrorResponse {
    // название ошибки
    private final String error;
    // подробное описание
    private final String description;

    // геттеры необходимы, чтобы Spring Boot мог получить значения полей
    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}