package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.GenreResponseDTO;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    //Получение жанра
    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> getGenre(@PathVariable Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(genreService.getGenre(id));
    }

    //Получение списка всех жанров
    @GetMapping
    public ResponseEntity<Set<GenreResponseDTO>> listGenres() {
        Set<GenreResponseDTO> set = genreService.listGenres();
        System.out.println("XXX "+set);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(set);
    }
}
