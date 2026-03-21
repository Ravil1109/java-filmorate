package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final BaseHandler<User> users = new BaseHandler<>();

    public User create(@Valid User user) {
        validate(user);

        return users.create(user);
    }

    public User update(@Valid User user) {
        validate(user);

        return users.update(user);
    }

    public List<User> getUsers() {
        return users.getList();
    }

    private void validate(User user) throws ValidationException {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
