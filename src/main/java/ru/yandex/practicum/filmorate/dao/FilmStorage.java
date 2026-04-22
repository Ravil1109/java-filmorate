package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> create(Film base);

    Optional<Film> update(Film base);

    void delete(Long id);

    List<Film> getList();

    Optional<Film> get(Long id);

    List<Long> getLikes(Long filmId);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getPopularFilms(Integer count);
}
