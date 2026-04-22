package ru.yandex.practicum.filmorate.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class UserResponseDTO {

    protected Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}


