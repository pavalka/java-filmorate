package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
class InDbLikeStorageTest {
    private final LikesStorage likesStorage;
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;

    private final User userOne;
    private final User userTwo;
    private final Film filmOne;
    private final Film filmTwo;

    private void addUserToDb() {
        jdbcTemplate.update("INSERT INTO users VALUES (1, 'user1@email.ru', 'user1login', 'user1 name', " +
                            "DATE '1985-3-6')");
    }

    private void addFilmToDb() {
        jdbcTemplate.update("INSERT INTO films VALUES (1, 'Film 1', 'Description 1', 93, 5, 1, DATE '1990-5-23')");
    }

    @AfterEach
    void deleteUsersAndFilms() {
        jdbcTemplate.update("DELETE FROM films");
        jdbcTemplate.update("DELETE FROM users");
    }

    @Autowired
    public InDbLikeStorageTest(LikesStorage likesStorage, JdbcTemplate jdbcTemplate,
                               @Qualifier("inDbFilmStorage") FilmStorage filmStorage) {
        this.likesStorage = likesStorage;
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
        userOne = new User("user1@email.ru", "user1login", LocalDate.of(1984, 3, 6));
        userOne.setId(1);
        userOne.setName("user1 name");
        userTwo = new User("user2@email.ru", "user2login", LocalDate.of(1985, 5, 26));
        userTwo.setId(2);
        userTwo.setName("user2 name");
        filmOne = new Film("Film 1", "Description 1", LocalDate.of(1990, 5, 23), 93);
        filmOne.setId(1);
        filmOne.setRate(5);
        filmOne.setMpa(new Mpa(1, "G"));
        filmTwo = new Film("Film 2", "Description 2", LocalDate.of(1990, 10, 23), 100);
        filmTwo.setId(2);
        filmTwo.setRate(4);
        filmTwo.setMpa(new Mpa(1, "G"));
    }

    @Test
    void addLikeShouldAddLike() {
        addFilmToDb();
        addUserToDb();

        boolean res = Assertions.assertDoesNotThrow(() -> likesStorage.addLike(filmOne, userOne));

        Assertions.assertTrue(res);

        var users = likesStorage.getUsersByFilmId(filmOne.getId());

        Assertions.assertEquals(1, users.size());
        Assertions.assertTrue(users.contains(userOne));
        Assertions.assertEquals(6, filmStorage.get(filmOne.getId()).get().getRate());
    }

    @Test
    void addLikesShouldNotAddLikeAtSecondTime() {
        addUserToDb();
        addFilmToDb();
        Assertions.assertDoesNotThrow(() -> likesStorage.addLike(filmOne, userOne));

        boolean res = Assertions.assertDoesNotThrow(() -> likesStorage.addLike(filmOne, userOne));

        Assertions.assertFalse(res);
        Assertions.assertEquals(1, likesStorage.getUsersByFilmId(filmOne.getId()).size());
        Assertions.assertEquals(6, filmStorage.get(filmOne.getId()).get().getRate());
    }

    @Test
    void deleteLikeShouldDeleteLike() {
        addFilmToDb();
        addUserToDb();
        likesStorage.addLike(filmOne, userOne);

        boolean res = Assertions.assertDoesNotThrow(() -> likesStorage.deleteLike(filmOne, userOne));

        Assertions.assertTrue(res);
        Assertions.assertTrue(likesStorage.getUsersByFilmId(filmOne.getId()).isEmpty());
        Assertions.assertEquals(5, filmStorage.get(filmOne.getId()).get().getRate());
    }

    @Test
    void deleteLikeShouldNotDeleteLikeWhenUserIsWrong() {
        addFilmToDb();
        addUserToDb();
        likesStorage.addLike(filmOne, userOne);

        boolean res = Assertions.assertDoesNotThrow(() -> likesStorage.deleteLike(filmOne, userTwo));

        Assertions.assertFalse(res);
        Assertions.assertEquals(1, likesStorage.getUsersByFilmId(filmOne.getId()).size());
        Assertions.assertEquals(6, filmStorage.get(filmOne.getId()).get().getRate());
    }

    @Test
    void deleteLikeShouldNotDeleteLikeWhenFilmIsWrong() {
        addFilmToDb();
        addUserToDb();
        likesStorage.addLike(filmOne, userOne);

        boolean res = Assertions.assertDoesNotThrow(() -> likesStorage.deleteLike(filmTwo, userOne));

        Assertions.assertFalse(res);
        Assertions.assertEquals(1, likesStorage.getUsersByFilmId(filmOne.getId()).size());
        Assertions.assertEquals(6, filmStorage.get(filmOne.getId()).get().getRate());
    }

    @Test
    void getUsersByFilmIdShouldReturnEmptyCollection() {
        var users = Assertions.assertDoesNotThrow(() -> likesStorage.getUsersByFilmId(filmOne.getId()));

        Assertions.assertTrue(users.isEmpty());
    }

    @Test
    void getUsersByFilmIdShouldReturnUsers() {
        addFilmToDb();
        addUserToDb();
        likesStorage.addLike(filmOne, userOne);

        var users = Assertions.assertDoesNotThrow(() -> likesStorage.getUsersByFilmId(filmOne.getId()));

        Assertions.assertEquals(1, users.size());
        Assertions.assertTrue(users.contains(userOne));
    }
}