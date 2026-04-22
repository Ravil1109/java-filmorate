package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.dto.FilmAddRequestDTO;
import ru.yandex.practicum.filmorate.dto.FilmResponseDTO;
import ru.yandex.practicum.filmorate.dto.FilmUpdRequestDTO;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapping.FilmMapping;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final FilmStorage storageFilms;
    private final UserStorage storageUsers;
    private final MpaStorage storageMpa;

    public FilmResponseDTO create(@Valid FilmAddRequestDTO filmDto) throws Exception {
        Film film = FilmMapping.dtoToModal(filmDto);
        validate(film);

        Optional<Film> target = storageFilms.create(film);
        log.info("Фильм добавлен {}", target.get());

        return FilmMapping.modalToDto(target.get());
    }

    public FilmResponseDTO update(@Valid FilmUpdRequestDTO filmDto) throws Exception {
        Film film = FilmMapping.dtoToModal(filmDto);
        validate(film);

        Optional<Film> target = storageFilms.update(film);
        validateOptional(target, filmDto.getId());
        log.info("Фильм обновлен {}", target.get());

        return FilmMapping.modalToDto(target.get());
    }

    public void delete(@Valid Long filmId) throws Exception {
        Optional<Film> target = storageFilms.get(filmId);
        validateOptional(target, filmId);

        storageFilms.delete(filmId);

        log.info("Фильм с идентификатором {} успешно удален", filmId);
    }

    public FilmResponseDTO getFilm(Long filmId) throws Exception {
        Optional<Film> target = storageFilms.get(filmId);
        validateOptional(target, filmId);

        return FilmMapping.modalToDto(target.get());
    }

    public List<FilmResponseDTO> listFilms() {
        return FilmMapping.listModalToDto(storageFilms.getList());
    }

    public void addLike(Long filmId, Long userId) throws Exception {
        Optional<Film> target = storageFilms.get(filmId);
        validateOptional(target, filmId);

        Optional<User> user = storageUsers.get(userId);
        UserService.validateOptional(user, userId);

        storageFilms.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) throws Exception {
        Optional<User>  user = storageUsers.get(userId);
        UserService.validateOptional(user, userId);

        storageFilms.deleteLike(filmId, userId);
    }

    public List<FilmResponseDTO> getPopularFilms(Integer count) {
        return FilmMapping.listModalToDto(storageFilms.getPopularFilms(count));
    }

    private void validate(Film film) throws ValidationException {
        if (film.getMpa() != null) {
            storageMpa.get(film.getMpa().getId());
        }

        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.error("Дата релиза должны быть не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза должны быть не раньше 28 декабря 1895 года");
        }
    }

    public static void validateOptional(Optional<Film> film, Long filmId) throws Exception {

        if (film.isEmpty()) {
            log.error("Фильм с идентификатором {} не найден", filmId);
            throw new NotFoundException(String.format("Фильм с идентификатором %d не найден", filmId));
        }
    }
}
