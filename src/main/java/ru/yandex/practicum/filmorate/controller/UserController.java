package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //Создание пользователя;
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        log.info("Начато создание пользователя {}", user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.create(user));
    }

    //Обновление пользователя;
    @PutMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {

        log.info("Начато обновление пользователя {}", user);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.update(user));
    }

    //Получение пользователя.
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getUser(id));
    }

    //Получение списка всех пользователей.
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getUsers());
    }

    // Добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Пользователь {} добавляет в друзья пользователя {}", id, friendId);
        userService.addFriend(id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Пользователь {} удаляет из друзей пользователя {}", id, friendId);
        userService.removeFriend(id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Получение списка друзей пользователя
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Integer id) {
        log.info("Запрос списка друзей пользователя {}", id);
        return new ResponseEntity<>(userService.getFriends(id), HttpStatus.OK);
    }

    // Получение списка общих друзей с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Запрос общих друзей пользователей {} и {}", id, otherId);
        return new ResponseEntity<>(userService.getCommonFriends(id, otherId), HttpStatus.OK);
    }
}
