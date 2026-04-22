package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    public List<Genre> getList();

    public Genre get(Long genreId);

    public List<Genre> getList(Long filmId);

    public void saveGenres(Film film);
}
