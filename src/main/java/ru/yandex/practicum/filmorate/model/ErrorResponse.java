package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class ErrorResponse {
    private final String error;
    private final String description;

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}