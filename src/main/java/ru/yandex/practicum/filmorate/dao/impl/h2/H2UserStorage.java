package ru.yandex.practicum.filmorate.dao.impl.h2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.dao.impl.h2.mappers.UserStorageMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.entity.UserEntity;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Primary
@Slf4j
@Component
@RequiredArgsConstructor
public class H2UserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorageMapper mapper;

    @Override
    public Optional<User> create(User user) {
        UserEntity entity = mapper.toEntity(user);

        String query = "insert into users (login, name, email, birthday) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(query, new String[]{"id"});
            stmt.setString(1, entity.getLogin());
            stmt.setString(2, entity.getName());
            stmt.setString(3, entity.getEmail());
            stmt.setDate(4, Date.valueOf(entity.getBirthday()));

            return stmt;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey().longValue()));

        return get(user.getId());
    }

    @Override
    public Optional<User> update(User user) {
        if (user.getId() == null) {
            log.error("Пользователь с идентификатором {} не найден", user.getId());
            throw new NotFoundException(String.format("Пользователь с идентификатором %d не найден", user.getId()));
        }

        String query = "update users set login = ?, name = ?, email = ?, birthday = ? " +
                " where id = ?";

        int updated = jdbcTemplate.update(query,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());

        if (updated == 0) {
            log.error("Пользователь с идентификатором {} не найден", user.getId());
            throw new NotFoundException(String.format("Пользователь с идентификатором %d не найден", user.getId()));
        }

        return get(user.getId());
    }

    @Override
    public List<User> getList() {
        String sql = "select * from users";
        List<UserEntity> entityList = jdbcTemplate.query(sql, mapper);
        return entityList.stream().map(entity -> {
            User user = mapper.toModel(entity);
            return user;
        }).toList();
    }

    @Override
    public Optional<User> get(Long id) {
        String sql = "select * from users where id = ?";
        UserEntity entity = null;

        try {
            entity = jdbcTemplate.queryForObject(sql, mapper, id);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        User user = mapper.toModel(entity);
        return Optional.of(user);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        List<User> friends = getFriends(userId);

        if (!friends.contains(friendId)) {
            String query = "insert into friends (user_id, friend_id) " +
                    "values (?, ?)";

            jdbcTemplate.update(query, userId, friendId);

        } else {
            log.info("Пользователь {} уже в друзьях у {}", friendId, userId);
        }

    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        List<User> friends = getFriends(userId);


        if (friends.contains(User.builder().id(friendId).build())) {
            String query = "delete from friends " +
                    "where user_id = ? and friend_id = ?";
            jdbcTemplate.update(query, userId, friendId);
        }
    }

    @Override
    public List<User> getFriends(Long id) {
        String sql = "select u.* " +
                "from friends fr " +
                "   , users u " +
                "where fr.friend_id =u.id " +
                "and fr.user_id = ?";
        List<UserEntity> entityList = jdbcTemplate.query(sql, mapper, id);

        return entityList.stream().map(entity -> {
            User user = mapper.toModel(entity);
            return user;
        }).toList();
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        String sql = "select u.* " +
                "from friends fr " +
                "   , friends fr2 " +
                "   , users u " +
                "where fr.friend_id = fr2.friend_id " +
                "and fr.user_id = ? " +
                "and fr2.user_id = ? " +
                "and fr.friend_id = u.id  ";
        List<UserEntity> entityList = jdbcTemplate.query(sql, mapper, userId, otherId);

        return entityList.stream().map(entity -> {
            User user = mapper.toModel(entity);
            return user;
        }).toList();
    }
}
