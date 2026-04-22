package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.dto.UserAddRequestDTO;
import ru.yandex.practicum.filmorate.dto.UserResponseDTO;
import ru.yandex.practicum.filmorate.dto.UserUpdRequestDTO;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapping.UserMapping;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage storageUsers;

    public UserResponseDTO create(@Valid UserAddRequestDTO userDto) {
        User user = UserMapping.dtoToModal(userDto);
        validate(user);

        Optional<User> target = storageUsers.create(user);
        log.info("Пользователь сохранен {}", user);

        return UserMapping.modalToDto(target.get());
    }

    public UserResponseDTO update(@Valid UserUpdRequestDTO userDto) {
        User user = UserMapping.dtoToModal(userDto);

        validate(user);

        Optional<User> target = storageUsers.update(user);
        log.info("Пользователь обновлен {}", user);

        return UserMapping.modalToDto(target.get());
    }

    public UserResponseDTO getUser(Long userId) throws Exception {
        Optional<User> target = storageUsers.get(userId);
        validateOptional(target, userId);

        return UserMapping.modalToDto(target.get());
    }

    public List<UserResponseDTO> listUsers() {
        return UserMapping.listModalToDto(storageUsers.getList());
    }

    public void addFriend(Long userId, Long friendId) throws Exception {
        Optional<User> user = storageUsers.get(userId);
        Optional<User> friend = storageUsers.get(friendId);
        validateOptional(user, userId);
        validateOptional(friend, friendId);

        storageUsers.addFriend(userId, friendId);

        log.info("Пользователь {} и пользователь {} теперь друзья", userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) throws Exception {
        Optional<User> user = storageUsers.get(userId);
        Optional<User> friend = storageUsers.get(friendId);
        validateOptional(user, userId);
        validateOptional(friend, friendId);

        storageUsers.deleteFriend(userId, friendId);

        log.info("Пользователь {} и пользователь {} больше не друзья", userId, friendId);
    }

    public List<UserResponseDTO> getFriends(Long userId) throws Exception {
        Optional<User> user = storageUsers.get(userId);
        validateOptional(user, userId);

        List<User> list = storageUsers.getFriends(userId);

        return UserMapping.listModalToDto(list);
    }

    public List<UserResponseDTO> getCommonFriends(Long userId, Long otherId) throws Exception {
        Optional<User> user = storageUsers.get(userId);
        Optional<User> otherUser = storageUsers.get(otherId);
        validateOptional(user, userId);
        validateOptional(otherUser, otherId);

        return UserMapping.listModalToDto(storageUsers.getCommonFriends(userId, otherId));
    }

    private void validate(User user) throws ValidationException {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (user.getLogin().contains(" ")) {
            log.error("Логин не должен содержать пробелы");
            throw new ValidationException("Логин не должен содержать пробелы");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public static void validateOptional(Optional<User> user, Long userId) throws Exception {

        if (user.isEmpty()) {
            log.error("Пользователь с идентификатором {} не найден", userId);
            throw new NotFoundException(String.format("Пользователь с идентификатором %d не найден", userId));
        }
    }
}
