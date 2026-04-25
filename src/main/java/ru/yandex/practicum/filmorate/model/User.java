package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
    protected Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private List<Long> friends = new ArrayList<>();
}


