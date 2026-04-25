package ru.yandex.practicum.filmorate.dao.impl.h2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dao.impl.h2.mappers.FilmStorageMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.entity.FilmEntity;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Primary
@RequiredArgsConstructor
@Repository
public class H2FilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorageMapper mapper;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    @Override
    public Optional<Film> create(Film film) {
        FilmEntity entity = mapper.toEntity(film);

        String query = "insert into films (name, description, release_date, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(query, new String[]{"id"});
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
            stmt.setDate(3, Date.valueOf(entity.getReleaseDate()));
            stmt.setInt(4, entity.getDuration());

            if (entity.getMpaId() != null) {
                stmt.setLong(5, entity.getMpaId());
            }

            return stmt;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey().longValue()));

        genreStorage.saveGenres(film);
        film.setGenres(genreStorage.getList(film.getId()));

        return get(film.getId());
    }

    @Override
    public Optional<Film> update(Film film) {
        if (film.getId() == null) {
            log.error("Фильм с идентификатором {} не найден", film.getId());
            throw new NotFoundException(String.format("Фильм с идентификатором %d не найден", film.getId()));
        }

        String query = "update films set name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id = ? WHERE id = ?";

        int updated = jdbcTemplate.update(query,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate() != null ? Date.valueOf(film.getReleaseDate()) : null,
                film.getDuration(),
                (film.getMpa() != null ? film.getMpa().getId() : null),
                film.getId());

        if (updated == 0) {
            log.error("Фильм с идентификатором {} не найден", film.getId());
            throw new NotFoundException(String.format("Фильм с идентификатором %d не найден", film.getId()));
        }

        return get(film.getId());
    }

    @Override
    public void delete(Long id) {
        String query = "delete from films where id = ?";
        int deleted = jdbcTemplate.update(query, id);
        if (deleted == 0) {
            log.error("Не удалось удалить фильм с идентификатором {}", id);
            throw new NotFoundException(String.format("Не удалось удалить фильм с идентификатором %d", id));
        }
    }

    @Override
    public List<Film> getList() {
        String query = "select * from films";
        List<FilmEntity> entityList = jdbcTemplate.query(query, mapper);
        return entityList.stream().map(entity -> {
            Film film = mapper.toModel(entity);
            film.setGenres(genreStorage.getList(entity.getId()));
            film.setMpa(entity.getMpaId() != null ? mpaStorage.get(entity.getMpaId()) : null);
            return film;
        }).toList();
    }

    @Override
    public Optional<Film> get(Long id) {
        String query = "select * from films where id = ?";
        FilmEntity entity = null;

        try {
            entity = jdbcTemplate.queryForObject(query, mapper, id);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        Film film = mapper.toModel(entity);
        film.setGenres(genreStorage.getList(entity.getId()));
        film.setMpa(entity.getMpaId() != null ? mpaStorage.get(entity.getMpaId()) : null);
        return Optional.of(film);
    }

    @Override
    public Set<Long> getLikes(Long filmId) {
        String query = "select user_id from film_likes where film_id = ?";
        List<Long> entityList = jdbcTemplate.query(query, (rs, rowNum) -> rs.getLong("user_id"), filmId);
        return entityList.stream().collect(Collectors.toSet());
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        Set<Long> likes = getLikes(filmId);

        if (!likes.contains(userId)) {
            String query = "insert into film_likes (film_id, user_id) " +
                    "values (?, ?)";

            jdbcTemplate.update(query, filmId, userId);

            log.info("Пользователь {} поставил лайк к фильму {}", userId, filmId);
        } else {
            log.info("Пользователь {} ранее уже ставил лайк к фильму {}", userId, filmId);
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        Optional<Film> film = get(filmId);

        Set<Long> likes = getLikes(filmId);
        if (!likes.contains(userId)) {
            log.error("Пользователь {} ранее не ставил лайк к фильму {}", userId, filmId);
            throw new NotFoundException(String.format("Не удалось удалить лайк пользователя %d фильма %d", userId, filmId));
        }

        String query = "delete from film_likes where film_id = ? and user_id = ?";
        int deleted = jdbcTemplate.update(query, filmId, userId);
        if (deleted == 0) {
            log.error("Не удалось удалить лайк пользователя {} к фильму {}", userId, filmId);
            throw new NotFoundException(String.format("Не удалось удалить лайк пользователя %d фильма %d", userId, filmId));
        }

        log.info("Лайк пользователя {} к фильму {} удален", userId, filmId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String query = "select f.* " +
                "from (select film_id, count(*) cnt " +
                "      from film_likes " +
                "      group by film_id " +
                "      order by cnt desc) l " +
                "join films f " +
                "on l.film_id  = f.id " +
                "limit ?";

        List<FilmEntity> entityList = jdbcTemplate.query(query, mapper, count);
        return entityList.stream().map(entity -> {
            Film film = mapper.toModel(entity);
            film.setGenres(genreStorage.getList(entity.getId()));
            film.setMpa(entity.getMpaId() != null ? mpaStorage.get(entity.getMpaId()) : null);
            return film;
        }).toList();
    }
}
