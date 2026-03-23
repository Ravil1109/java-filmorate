package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

/**
 * Film.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film extends BaseModel {
    @NotBlank
    private String name; //Название
    @Size(max = 200)
    private String description;    //Описание
    @NonNull
    private LocalDate releaseDate; //Дата релиза
    @Min(1)
    private Integer duration;      //Продолжительность фильма
}