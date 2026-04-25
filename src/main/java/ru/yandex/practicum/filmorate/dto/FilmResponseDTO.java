package ru.yandex.practicum.filmorate.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class FilmResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private MpaResponseDTO mpa;
    private Set<GenreResponseDTO> genres = new HashSet<>();

}