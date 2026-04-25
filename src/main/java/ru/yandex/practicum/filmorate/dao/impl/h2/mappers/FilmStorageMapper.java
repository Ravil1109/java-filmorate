package ru.yandex.practicum.filmorate.dao.impl.h2.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.entity.FilmEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FilmStorageMapper implements RowMapper<FilmEntity> {

    private final MpaStorageMapper ratingMapper;

    @Override
    public FilmEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmEntity film = FilmEntity
                .builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .mpaId(resultSet.getLong("mpa_id"))
                .build();

        return film;
    }

    public FilmEntity toEntity(Film film) {
        if (film == null) {
            return null;
        }

        return FilmEntity.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .mpaId(film.getMpa() != null ? film.getMpa().getId() : null)
                .build();
    }

    public Film toModel(FilmEntity entity) {
        if (entity == null) {
            return null;
        }

        return Film.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .duration(entity.getDuration())
                .releaseDate(entity.getReleaseDate() != null ? entity.getReleaseDate() : null)
                .build();
    }
}
