package ru.yandex.practicum.filmorate.dao.impl.h2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dao.impl.h2.mappers.MpaStorageMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.entity.MpaEntity;

import java.util.List;

@Slf4j
@Primary
@RequiredArgsConstructor
@Repository
public class H2MpaStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorageMapper mapper;

    @Override
    public List<Mpa> getList() {
        String sql = "select * from mpa order by id";
        List<MpaEntity> entityList = jdbcTemplate.query(sql, mapper);
        return entityList.stream().map(mapper::toModel).toList();
    }

    @Override
    public Mpa get(Long mpaId) {
        if (mpaId == null) {
            return null;
        }

        String sql = "select * from mpa where id = ? order by id";
        MpaEntity entity = null;

        try {
            entity = jdbcTemplate.queryForObject(sql, mapper, mpaId);

        } catch (
                EmptyResultDataAccessException e) {
            log.error("Рейтинг с идентификатором {} не найден", mpaId);
            throw new NotFoundException(String.format("Рейтинг с идентификатором %d не найден", mpaId));
        }

        return mapper.toModel(entity);
    }
}
