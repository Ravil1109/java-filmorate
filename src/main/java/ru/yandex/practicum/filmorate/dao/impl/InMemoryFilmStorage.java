package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.Storage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements Storage<Film> {
    private final Map<Integer, Film> map = new HashMap<>();

    @Override
    public Film create(Film base) {
        Integer idx = map.size() + 1;
        base.setId(idx);

        map.put(idx, base);

        log.info("Пользователь сохранен {}", base);
        return base;
    }

    @Override
    public Film update(Film base) {

        Integer index = base.getId();
        if (!map.containsKey(index)) {
            log.error("Фильм с идентификатором {} не найдена", index);
            throw new NotFoundException(String.format("Фильм с идентификатором %d не найдена", index));
        }

        map.put(index, base);

        log.info("Фильм обновлен {}", base);
        return base;
    }

    @Override
    public void delete(Integer index) {
        if (!map.containsKey(index)) {
            map.remove(index);
            log.info("Фильм с идентификатором {} удален", index);
        } else {
            log.error("Фильм с идентификатором {} не найдена", index);
        }
    }

    @Override
    public List<Film> getList() {
        return map.values().stream().toList();
    }

    public Film getFilmById(Integer id) {
        Film film = map.get(id);
        if (film == null) {
            log.error("Фильм с идентификатором {} не найден", id);
            throw new NotFoundException(String.format("Фильм с идентификатором %d не найден", id));
        }
        return film;
    }
}
