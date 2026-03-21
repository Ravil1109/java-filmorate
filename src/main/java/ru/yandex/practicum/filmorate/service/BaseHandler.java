package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaseHandler<T extends BaseModel> {
    private final Map<Integer, T> map = new HashMap<>();

    public T create(T base) {
        Integer idx = map.size() + 1;
        base.setId(idx);

        map.put(idx, base);
        if (base instanceof Film) {
            log.info("Фильм сохранен {}", base);
        } else if (base instanceof User) {
            log.info("Фильм сохранен {}", base);
        }

        return map.get(idx);
    }

    public T update(T base) throws ValidationException {
        Integer idx = base.getId();
        if (!map.containsKey(idx)) {
            log.error("Сущность с идентификатором {} не найдена", idx);
            throw new ValidationException(String.format("Сущность с идентификатором %d не найдена", idx));
        }

        map.put(idx, base);
        if (base instanceof Film) {
            log.info("Фильм обновлен {}", base);
        } else if (base instanceof User) {
            log.info("Пользователь обновлен {}", base);
        }

        return map.get(idx);
    }

    public List<T> getList() {
        return map.values().stream().toList();
    }
}
