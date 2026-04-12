package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    public Film create(Film base);

    public Film update(Film base);

    public void delete(Integer index);

    public List<Film> getList();
}
