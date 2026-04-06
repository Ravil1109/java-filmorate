package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    //Добавление фильма;
    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        log.info("Начато создание фильма {}", film);

        return new ResponseEntity(filmService.create(film), HttpStatus.CREATED);
    }

    //Обновление фильма;
    @PutMapping
    public ResponseEntity<Film> saveFilm(@Valid @RequestBody Film film) {
        log.info("Начато обновление фильма {}", film);

        return new ResponseEntity(filmService.update(film), HttpStatus.OK);
    }

    //Получение всех фильмов
    @GetMapping
    public ResponseEntity<List<Film>> getFilms() {
        log.info("Начато получение всех фильмов");
        return new ResponseEntity(filmService.getFilms(), HttpStatus.OK);
    }

    // Пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Пользователь {} ставит лайк фильму {}", userId, id);
        filmService.addLike(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Пользователь удаляет лайк
    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Пользователь {} удаляет лайк у фильма {}", userId, id);
        filmService.removeLike(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Получение списка популярных фильмов
    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        log.info("Запрос списка популярных фильмов, count={}", count);
        return new ResponseEntity<>(filmService.getPopularFilms(count), HttpStatus.OK);
    }
}
