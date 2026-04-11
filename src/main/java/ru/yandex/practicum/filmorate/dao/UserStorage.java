package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {
    public User create(User base);

    public User update(User base);

    public void delete(Integer index);

    public List<User> getList();
}
