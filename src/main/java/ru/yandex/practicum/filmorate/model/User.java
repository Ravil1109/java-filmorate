package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * User.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseModel {
    @NotBlank
    @Email
    private String email; //Электронная почта
    @NotBlank
    private String login; //Логин пользователя
    private String name;  //Имя для отображения
    @NonNull
    private LocalDate birthday; //Дата рождения
    @Builder.Default
    private Set<Integer> friends = new HashSet<>(); // Множество ID друзей пользователя
}


