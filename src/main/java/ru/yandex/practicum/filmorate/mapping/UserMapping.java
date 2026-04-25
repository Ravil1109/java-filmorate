package ru.yandex.practicum.filmorate.mapping;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dto.*;

import java.util.List;

public class UserMapping {
    public static User dtoToModal(UserAddRequestDTO userDto) {
        if (userDto == null) {
            return null;
        }

        User user = User.builder()
                .login(userDto.getLogin())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .birthday(userDto.getBirthday())
                .build();

        return user;
    }

    public static User dtoToModal(UserUpdRequestDTO userDto) {
        if (userDto == null) {
            return null;
        }

        User user = User.builder()
                .id(userDto.getId())
                .login(userDto.getLogin())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .birthday(userDto.getBirthday())
                .build();

        return user;
    }

    public static UserResponseDTO modalToDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO filmDto = UserResponseDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .build();

        return filmDto;
    }

    public static List<UserResponseDTO> listModalToDto(List<User> users) {
        if (users == null) {
            return null;
        }

        return users.stream().map(UserMapping::modalToDto).toList();
    }
}
