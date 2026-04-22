package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class FilmUpdRequestDTO {
    @NonNull
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @Min(1)
    private Integer duration;

    private MpaRequestDTO mpa;

    @Builder.Default
    private List<GenreRequestDTO> genres = new ArrayList<>();

}