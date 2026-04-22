package ru.yandex.practicum.filmorate.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class FilmResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private MpaResponseDTO mpa;
    private List<GenreResponseDTO> genres = new ArrayList<>();

}