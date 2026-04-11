package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.dao.impl.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final InMemoryFilmStorage storageFilms;
    private final InMemoryUserStorage storageUsers;

    public Film create(@Valid Film film) {
        validate(film);
        log.info("Фильм сохранен {}", film);
        return storageFilms.create(film);
    }

    public Film update(@Valid Film film) {
        validate(film);
        return storageFilms.update(film);
    }

    public Film getFilm(Integer filmId) {
        return storageFilms.getFilmById(filmId);
    }

    public List<Film> getFilms() {
        return storageFilms.getList();
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = storageFilms.getFilmById(filmId);
        User user = storageUsers.getUserById(userId); //Необходимо для проверки пользователя в хранилище

        if (!film.getLikes().contains(userId)) {
            film.getLikes().add(userId);
            log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
        } else {
            log.info("Пользователь {} ранее уже ставил лайк к фильму {}", userId, filmId);
        }
    }

    public void removeLike(Integer filmId, Integer userId) {
        Film film = storageFilms.getFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            log.warn("Пользователь {} не ставил лайк фильму {}", userId, filmId);
            throw new NotFoundException("Пользователь не ставил лайк этому фильму");
        }
        film.getLikes().remove(userId);
        log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return storageFilms.getList().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size())) // Сортировка по убыванию количества лайков
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.error("Дата релиза должны быть не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза должны быть не раньше 28 декабря 1895 года");
        }
    }
}
