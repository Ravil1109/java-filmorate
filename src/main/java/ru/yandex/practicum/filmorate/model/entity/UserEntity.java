package ru.yandex.practicum.filmorate.model.entity;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private Long id;

    private String login;

    private String email;

    private String name;

    private LocalDate birthday;

}


