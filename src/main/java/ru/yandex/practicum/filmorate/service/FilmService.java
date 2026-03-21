package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 8);
    private final BaseHandler<Film> films = new BaseHandler<>();

    public Film create(@Valid Film film) {
        validate(film);
        return films.create(film);
    }

    public Film update(@Valid Film film) {
        validate(film);
        return films.update(film);
    }

    public List<Film> getFilms() {
        return films.getList();
    }

    private void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.error("Дата релиза должны быть не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза должны быть не раньше 28 декабря 1895 года");
        }
    }
}
