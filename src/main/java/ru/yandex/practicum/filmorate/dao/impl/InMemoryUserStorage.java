package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.Storage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements Storage<User> {
    private final Map<Integer, User> map = new HashMap<>();

    @Override
    public User create(User base) {
        Integer idx = map.size() + 1;
        base.setId(idx);

        map.put(idx, base);

        log.info("Пользователь сохранен {}", base);
        return base;
    }

    @Override
    public User update(User base) {
        Integer index = base.getId();
        if (!map.containsKey(index)) {
            log.error("Пользователь с идентификатором {} не найдена", index);
            throw new NotFoundException(String.format("Пользователь с идентификатором %d не найдена", index));
        }

        map.put(index, base);

        log.info("Пользователь обновлен {}", base);
        return base;
    }

    @Override
    public void delete(Integer index) {
        if (!map.containsKey(index)) {
            map.remove(index);
            log.info("Пользователь с идентификатором {} удален", index);
        } else {
            log.error("Пользователь с идентификатором {} не найдена", index);
        }
    }

    @Override
    public List<User> getList() {
        return map.values().stream().toList();
    }

    public User getUserById(Integer id) {
        User user = map.get(id);
        if (user == null) {
            log.error("Пользователь с идентификатором {} не найден", id);
            throw new NotFoundException(String.format("Пользователь с идентификатором %d не найден", id));
        }
        return user;
    }

}
