package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Set;

public interface MpaStorage {
    public Set<Mpa> getList();

    public Mpa get(Long mpaId);
}
