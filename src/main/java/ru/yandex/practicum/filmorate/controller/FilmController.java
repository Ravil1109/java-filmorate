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
        try {
            return new ResponseEntity(filmService.create(film), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Обновление фильма;
    @PutMapping
    public ResponseEntity<Film> saveFilm(@Valid @RequestBody Film film) {
        log.info("Начато обновление фильма {}", film);
        try {
            return new ResponseEntity(filmService.update(film), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Получение всех фильмов
    @GetMapping
    public ResponseEntity<List<Film>> getFilms() {
        log.info("Начато получение всех фильмов");
        try {
            return new ResponseEntity(filmService.getFilms(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
