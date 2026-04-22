package ru.yandex.practicum.filmorate.dao.impl.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> map = new HashMap<>();

    @Override
    public Optional<Film> create(Film film) {
        Long idx = map.size() + 1L;
        film.setId(idx);

        map.put(idx, film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> update(Film film) {

        Long index = film.getId();
        if (!map.containsKey(index)) {
            log.error("Фильм с идентификатором {} не найдена", index);
            throw new NotFoundException(String.format("Фильм с идентификатором %d не найдена", index));
        }

        map.put(index, film);
        return Optional.of(film);
    }

    @Override
    public void delete(Long index) {
        if (map.containsKey(index)) {
            map.remove(index);
            log.info("Фильм с идентификатором {} удален", index);
        }
    }

    @Override
    public List<Film> getList() {
        return map.values().stream().toList();
    }

    @Override
    public Optional<Film> get(Long id) {
        if (!map.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(map.get(id));
    }

    @Override
    public List<Long> getLikes(Long filmId) {
        return map.get(filmId).getLikes();
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        Film film = map.get(filmId);

        if (!film.getLikes().contains(userId)) {
            film.getLikes().add(userId);
            log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
        } else {
            log.info("Пользователь {} ранее уже ставил лайк к фильму {}", userId, filmId);
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        Film film = map.get(filmId);

        if (!film.getLikes().contains(userId)) {
            log.warn("Пользователь {} не ставил лайк фильму {}", userId, filmId);
            throw new NotFoundException("Пользователь не ставил лайк этому фильму");
        }
        film.getLikes().remove(userId);
        log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return getList().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size())) // Сортировка по убыванию количества лайков
                .limit(count)
                .toList();
    }
}
