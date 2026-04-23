package ru.yandex.practicum.filmorate.dao.impl.h2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.dao.impl.h2.mappers.GenreStorageMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.entity.GenreEntity;

import java.util.List;

@Slf4j
@Primary
@RequiredArgsConstructor
@Repository
public class H2GenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorageMapper mapper;

    @Override
    public List<Genre> getList() {
        String sql = "select * from genres order by id asc";
        List<GenreEntity> entityList = jdbcTemplate.query(sql, mapper);

        return entityList.stream().map(mapper::toModel).toList();
    }

    @Override
    public Genre get(Long genreId) {
        String sql = "select * from genres where id = ? order by id asc";

        GenreEntity entity = null;

        try {
            entity = jdbcTemplate.queryForObject(sql, mapper, genreId);
        } catch (EmptyResultDataAccessException e) {
            log.error("Жанр с идентификатором {} не найден", genreId);
            throw new NotFoundException(String.format("Жанр с идентификатором %d не найден", genreId));
        }

        return mapper.toModel(entity);
    }

    @Override
    public List<Genre> getList(Long filmId) {
        String sql = "select g.* " +
                "from film_genres fg, genres g " +
                "where fg.genre_id = g.id and fg.film_id = ? " +
                "order by g.id asc";
        List<GenreEntity> entityList = jdbcTemplate.query(sql, mapper, filmId);

        return entityList.stream().map(mapper::toModel).toList();
    }

    @Override
    public void saveGenres(Film film) {
        String queryDelete = "delete from film_genres where film_id = ?";
        jdbcTemplate.update(queryDelete, film.getId());

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }

        String queryInsert = "insert into film_genres (film_id, genre_id) values (?, ?)";

        film.getGenres().forEach(g -> {
            Genre genre = get(g.getId());

            jdbcTemplate.update(queryInsert, film.getId(), genre.getId());
        });
        log.debug("Для фильма с id {} сохранено {} жанров", film.getId(), film.getGenres().size());
    }
}
