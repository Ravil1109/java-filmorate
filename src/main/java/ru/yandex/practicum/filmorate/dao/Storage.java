package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.BaseModel;

import java.util.List;

public interface Storage<T extends BaseModel> {
    public T create(T base);

    public T update(T base);

    public void delete(Integer index);

    public List<T> getList();
}
