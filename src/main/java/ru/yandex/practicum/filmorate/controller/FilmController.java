package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmAddRequestDTO;
import ru.yandex.practicum.filmorate.dto.FilmResponseDTO;
import ru.yandex.practicum.filmorate.dto.FilmUpdRequestDTO;
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
    public ResponseEntity<FilmResponseDTO> addFilm(@Valid @RequestBody FilmAddRequestDTO filmDto) throws Exception {
        log.info("Начато создание фильма {}", filmDto);

        return new ResponseEntity(filmService.create(filmDto), HttpStatus.CREATED);
    }

    //Обновление фильма;
    @PutMapping
    public ResponseEntity<FilmResponseDTO> saveFilm(@Valid @RequestBody FilmUpdRequestDTO filmDto) throws Exception {
        log.info("Начато обновление фильма {}", filmDto);

        return new ResponseEntity(filmService.update(filmDto), HttpStatus.OK);
    }

    //Получение фильма.
    @GetMapping("/{id}")
    public ResponseEntity<FilmResponseDTO> getFilm(@PathVariable Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.getFilm(id));
    }

    //Получение всех фильмов
    @GetMapping
    public ResponseEntity<List<FilmResponseDTO>> listFilms() {
        log.info("Начато получение всех фильмов");
        return new ResponseEntity(filmService.listFilms(), HttpStatus.OK);
    }

    // Пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable Long id, @PathVariable Long userId) throws Exception {
        log.info("Пользователь {} ставит лайк фильму {}", userId, id);
        filmService.addLike(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Пользователь удаляет лайк
    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable Long id, @PathVariable Long userId) throws Exception {
        log.info("Пользователь {} удаляет лайк у фильма {}", userId, id);
        filmService.deleteLike(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Получение списка популярных фильмов
    @GetMapping("/popular")
    public ResponseEntity<List<FilmResponseDTO>> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        log.info("Запрос списка популярных фильмов, count={}", count);
        return new ResponseEntity<>(filmService.getPopularFilms(count), HttpStatus.OK);
    }
}
