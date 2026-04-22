package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserAddRequestDTO;
import ru.yandex.practicum.filmorate.dto.UserResponseDTO;
import ru.yandex.practicum.filmorate.dto.UserUpdRequestDTO;
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
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserAddRequestDTO user) {

        log.info("Начато создание пользователя {}", user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.create(user));
    }

    //Обновление пользователя;
    @PutMapping
    public ResponseEntity<UserResponseDTO> saveUser(@Valid @RequestBody UserUpdRequestDTO user) {

        log.info("Начато обновление пользователя {}", user);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.update(user));
    }

    //Получение пользователя.
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getUser(id));
    }

    //Получение списка всех пользователей.
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listUsers() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.listUsers());
    }

    // Добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable Long id, @PathVariable Long friendId) throws Exception {
        log.info("Пользователь {} добавляет в друзья пользователя {}", id, friendId);
        userService.addFriend(id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long id, @PathVariable Long friendId) throws Exception {
        log.info("Пользователь {} удаляет из друзей пользователя {}", id, friendId);
        userService.deleteFriend(id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Получение списка друзей пользователя
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<UserResponseDTO>> getFriends(@PathVariable Long id) throws Exception {
        log.info("Запрос списка друзей пользователя {}", id);

        List<UserResponseDTO> friends = userService.getFriends(id);

        log.info("Список друзей пользователя {}: {}", id, friends);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    // Получение списка общих друзей с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<UserResponseDTO>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) throws Exception {
        log.info("Запрос общих друзей пользователей {} и {}", id, otherId);
        return new ResponseEntity<>(userService.getCommonFriends(id, otherId), HttpStatus.OK);
    }
}
