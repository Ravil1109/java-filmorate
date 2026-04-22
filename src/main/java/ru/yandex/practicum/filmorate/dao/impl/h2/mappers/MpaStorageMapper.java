package ru.yandex.practicum.filmorate.dao.impl.h2.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.entity.MpaEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaStorageMapper implements RowMapper<MpaEntity> {

    @Override
    public MpaEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        MpaEntity rating = MpaEntity
                .builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();

        return rating;
    }

    public MpaEntity toEntity(Mpa mpa) {
        if (mpa == null) {
            return null;
        }

        return MpaEntity.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
    }

    public Mpa toModel(MpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Mpa.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}

