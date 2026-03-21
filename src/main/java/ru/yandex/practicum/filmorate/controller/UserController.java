package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        try {
            return new ResponseEntity(userService.create(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Обновление пользователя;
    @PutMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        log.info("Начато обновление пользователя {}", user);

        try {
            return new ResponseEntity(userService.update(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Получение списка всех пользователей.
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        try {
            return new ResponseEntity(userService.getUsers(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
