package ru.yandex.practicum.filmorate.dao.impl.h2.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.entity.UserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserStorageMapper implements RowMapper<UserEntity> {

    @Override
    public UserEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        UserEntity user = UserEntity
                .builder()
                .id(resultSet.getLong("id"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();

        return user;
    }

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        return UserEntity.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .build();
    }

    public User toModel(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = User.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .name(entity.getName())
                .email(entity.getEmail())
                .birthday(entity.getBirthday() != null ? entity.getBirthday() : null)
                .build();

        return user;
    }

}

