package ru.yandex.practicum.filmorate.exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {

        super(message);
    }

    public ValidationException(String message, Exception e) {
        super(message, e);
    }
}
