package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.dto.GenreResponseDTO;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage storage;

    public GenreResponseDTO getGenre(Long genreId) {
        Genre genre = storage.get(genreId);

        return GenreResponseDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public List<GenreResponseDTO> listGenres() {
        List<Genre> listGenre = storage.getList();
        return listGenre.stream().map(mpa -> GenreResponseDTO.builder()
                        .id(mpa.getId())
                        .name(mpa.getName())
                        .build())
                .toList();
    }
}
