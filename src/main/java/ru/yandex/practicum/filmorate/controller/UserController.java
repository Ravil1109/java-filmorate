package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
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
            return new ResponseEntity(userService.create(user), HttpStatus.CREATED);
        } catch (ValidationException e) {
            ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            detail.setTitle(e.getMessage());
            return ResponseEntity.of(detail).build();
        }
    }

    //Обновление пользователя;
    @PutMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        log.info("Начато обновление пользователя {}", user);
        try {
            return new ResponseEntity(userService.update(user), HttpStatus.OK);
        } catch (ValidationException e) {
            ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            detail.setTitle(e.getMessage());
            return ResponseEntity.of(detail).build();
        }
    }

    //Получение списка всех пользователей.
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity(userService.getUsers(), HttpStatus.OK);
    }
}
