package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage storageUsers;

    public User create(@Valid User user) {
        validate(user);

        return storageUsers.create(user);
    }

    public User update(@Valid User user) {
        validate(user);

        return storageUsers.update(user);
    }

    public User getUser(Integer userId) {
        return storageUsers.getUserById(userId);
    }

    public List<User> getUsers() {
        return storageUsers.getList();
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = storageUsers.getUserById(userId);
        User friend = storageUsers.getUserById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        log.info("Пользователь {} и пользователь {} теперь друзья", userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        User user = storageUsers.getUserById(userId);
        User friend = storageUsers.getUserById(friendId);

        if (!user.getFriends().contains(friendId)) {
            log.warn("Пользователь {} не является другом пользователя {}", friendId, userId);
            //throw new NotFoundException("Пользователь не является другом");
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        log.info("Пользователь {} и пользователь {} больше не друзья", userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        User user = storageUsers.getUserById(userId);

        return user.getFriends().stream()
                .map(storageUsers::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        User user = storageUsers.getUserById(userId);
        User otherUser = storageUsers.getUserById(otherId);

        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(storageUsers::getUserById)
                .collect(Collectors.toList());
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
}
