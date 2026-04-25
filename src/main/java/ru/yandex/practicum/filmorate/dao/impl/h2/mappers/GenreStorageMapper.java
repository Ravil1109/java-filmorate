package ru.yandex.practicum.filmorate.dao.impl.h2.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.entity.GenreEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreStorageMapper implements RowMapper<GenreEntity> {

    @Override
    public GenreEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        GenreEntity genre = GenreEntity
                .builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();

        return genre;
    }

    public Genre toModel(GenreEntity entity) {
        if (entity == null) {
            return null;
        }

        return Genre.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}

