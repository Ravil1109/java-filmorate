package ru.yandex.practicum.filmorate.dao.impl.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> map = new HashMap<>();

    @Override
    public Optional<User> create(User user) {
        Long idx = map.size() + 1L;
        user.setId(idx);

        map.put(idx, user);

        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        Long index = user.getId();
        if (!map.containsKey(index)) {
            return Optional.empty();
        }

        map.put(index, user);
        return Optional.of(user);
    }

    @Override
    public List<User> getList() {
        return map.values().stream().toList();
    }

    @Override
    public Optional<User> get(Long id) {
        User user = map.get(id);
        if (user == null) {
            log.error("Пользователь с идентификатором {} не найден", id);
            throw new NotFoundException(String.format("Пользователь с идентификатором %d не найден", id));
        }
        return Optional.of(user);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = map.get(userId);

        if (!user.getFriends().contains(friendId)) {
            user.getFriends().add(friendId);

        } else {
            log.info("Пользователь {} уже в друзьях у {}", friendId, userId);
        }

    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        User user = map.get(userId);

        if (user.getFriends().contains(friendId)) {
            user.getFriends().remove(friendId);
        }

    }

    @Override
    public List<User> getFriends(Long id) {
        User user = map.get(id);

        return user.getFriends().stream()
                .map(friendId -> map.get(friendId))
                .toList();
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = map.get(userId);
        User otherUser = map.get(otherId);

        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(friendId -> map.get(friendId))
                .toList();
    }
}
