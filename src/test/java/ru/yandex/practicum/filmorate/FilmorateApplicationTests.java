package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate.dao.impl.h2")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    private User testUser;
    private Film testFilm;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .login("testUser")
                .name("ТЕстовый пользователь")
                .email("test@example.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        testFilm = Film.builder()
                .name("Просто фильм")
                .description("Описание")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpa(new Mpa(1L, "G"))
                .build();
    }

    @Test
    void testCreateUser() {
        Optional<User> optUser = userStorage.create(testUser);

        assertTrue(optUser.isPresent());
        assertTrue(optUser.get().getId() != null);
    }

    @Test
    void testGetUserById() {
        Optional<User> optUser = userStorage.create(testUser);
        Long userId = optUser.get().getId();

        Optional<User> foundUser = userStorage.get(userId);

        assertTrue(foundUser.isPresent());
        assertTrue(foundUser.get().getId() != null);
        assertTrue("testUser".equals(foundUser.get().getLogin()));
    }

    @Test
    void testGetUserByIdNotFound() {
        Optional<User> optUser = userStorage.get(100L);
        assertTrue(optUser.isEmpty());
    }

    @Test
    void testUpdateUser() {
        Optional<User> optUser = userStorage.create(testUser);
        User userToUpdate = optUser.get();
        userToUpdate.setName("Новое имя");
        userToUpdate.setLogin("testUser2");

        Optional<User> updUser = userStorage.update(userToUpdate);

        assertTrue(updUser.isPresent());
        assertTrue("Новое имя".equals(updUser.get().getName()));
        assertTrue("testUser2".equals(updUser.get().getLogin()));
    }

    @Test
    void testGetUserList() {
        userStorage.create(testUser);
        User user2 = User.builder()
                .login("testUser2")
                .name("User2")
                .email("user2@example.com")
                .birthday(LocalDate.of(1995, 5, 5))
                .build();
        userStorage.create(user2);

        List<User> users = userStorage.getList();

        assertTrue(users.size() == 2);
    }

    @Test
    void testCreateFilm() {
        Optional<Film> optFilm = filmStorage.create(testFilm);

        assertTrue(optFilm.isPresent());
        assertTrue(optFilm.get().getId() != null);
        assertTrue("Просто фильм".equals(optFilm.get().getName()));
        assertTrue(Integer.valueOf(120).equals(optFilm.get().getDuration()));
    }

    @Test
    void testGetFilmById() {
        Optional<Film> created = filmStorage.create(testFilm);
        Long filmId = created.get().getId();

        Optional<Film> optFilm = filmStorage.get(filmId);

        assertTrue(optFilm.isPresent());
        assertTrue(filmId.equals(optFilm.get().getId()));
        assertTrue("Просто фильм".equals(optFilm.get().getName()));
    }

    @Test
    void testGetFilmByIdNotFound() {
        Optional<Film> optFilm = filmStorage.get(100L);

        assertTrue(optFilm.isEmpty());
    }

    @Test
    void testUpdateFilm() {
        Optional<Film> created = filmStorage.create(testFilm);
        Film filmToUpdate = created.get();
        filmToUpdate.setName("Просто фильм2");
        filmToUpdate.setDuration(150);

        Optional<Film> optFilm = filmStorage.update(filmToUpdate);

        assertTrue(optFilm.isPresent());
        assertTrue("Просто фильм2".equals(optFilm.get().getName()));
        assertTrue(Integer.valueOf(150).equals(optFilm.get().getDuration()));
    }

    @Test
    void testDeleteFilm() {
        Optional<Film> created = filmStorage.create(testFilm);
        Long filmId = created.get().getId();

        filmStorage.delete(filmId);

        Optional<Film> optFilm = filmStorage.get(filmId);
        assertTrue(optFilm.isEmpty());
    }

    @Test
    void testGetFilmList() {
        filmStorage.create(testFilm);
        Film secondFilm = Film.builder()
                .name("Просто фильм2")
                .description("Описание")
                .releaseDate(LocalDate.of(2021, 2, 2))
                .duration(130)
                .mpa(new Mpa(1L, "G"))
                .build();
        filmStorage.create(secondFilm);

        List<Film> films = filmStorage.getList();

        assertTrue(films.size() == 2);
    }

}
