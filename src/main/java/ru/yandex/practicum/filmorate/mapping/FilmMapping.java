package ru.yandex.practicum.filmorate.mapping;

import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;
import java.util.stream.Collectors;

public class FilmMapping {
    public static Film dtoToModal(FilmAddRequestDTO filmDto) {
        if (filmDto == null) {
            return null;
        }

        Film film = Film.builder()
                .name(filmDto.getName())
                .description(filmDto.getDescription())
                .releaseDate(filmDto.getReleaseDate())
                .duration(filmDto.getDuration())
                .likes(new HashSet<>())
                .build();

        if (filmDto.getMpa() != null) {
            film.setMpa(Mpa.builder()
                    .id(filmDto.getMpa().getId())
                    .build());
        }

        if (filmDto.getGenres() != null) {
            Set<Genre> genres = new HashSet<>();

            filmDto.getGenres().forEach(genreDto -> {
                Genre genre = Genre.builder()
                        .id(genreDto.getId())
                        .build();

                if (!genres.contains(genre)) {
                    genres.add(genre);
                }
            });

            film.setGenres(genres);
        }

        return film;
    }

    public static Film dtoToModal(FilmUpdRequestDTO filmDto) {
        if (filmDto == null) {
            return null;
        }

        Film film = Film.builder()
                .id(filmDto.getId())
                .name(filmDto.getName())
                .description(filmDto.getDescription())
                .releaseDate(filmDto.getReleaseDate())
                .duration(filmDto.getDuration())
                .likes(new HashSet<>())
                .build();

        if (filmDto.getMpa() != null) {
            film.setMpa(Mpa.builder()
                    .id(filmDto.getMpa().getId())
                    .build());
        }

        if (filmDto.getGenres() != null) {
            Set<Genre> genres = new HashSet<>();

            filmDto.getGenres().forEach(genreDto -> {
                if (!genres.contains(genreDto)) {
                    genres.add(Genre.builder()
                            .id(genreDto.getId())
                            .build());
                }
            });

            film.setGenres(genres);
        }

        return film;
    }

    public static FilmResponseDTO modalToDto(Film film) {
        if (film == null) {
            return null;
        }

        FilmResponseDTO filmDto = FilmResponseDTO.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();

        if (film.getMpa() != null) {
            filmDto.setMpa(MpaResponseDTO.builder()
                    .id(film.getMpa().getId())
                    .name(film.getMpa().getName())
                    .build());
        }

        if (film.getGenres() != null) {

            filmDto.setGenres(film.getGenres()
                    .stream()
                    .map(g -> GenreResponseDTO.builder()
                            .id(g.getId())
                            .name(g.getName())
                            .build())
                    .collect(Collectors.toSet()));

        }

        return filmDto;
    }

    public static List<FilmResponseDTO> listModalToDto(List<Film> films) {
        if (films == null) {
            return null;
        }

        return films.stream().map(FilmMapping::modalToDto).toList();
    }
}
