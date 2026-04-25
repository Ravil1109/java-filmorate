package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreStorage {
    public Set<Genre> getList();

    public Genre get(Long genreId);

    public Set<Genre> getList(Long filmId);

    public void saveGenres(Film film);
}
