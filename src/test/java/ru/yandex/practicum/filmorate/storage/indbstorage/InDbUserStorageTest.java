package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;


@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("in_db_storage")
class InDbUserStorageTest {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InDbUserStorageTest(UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    private void addUsersToDb() {
        jdbcTemplate.update("INSERT INTO users VALUES (1, 'user1@email.ru', 'user1', 'name 1', DATE '1980-02-03')");
        jdbcTemplate.update("INSERT INTO users VALUES (2, 'user2@email.ru', 'user2', 'name 2', DATE '1990-06-24')");
    }

    @AfterEach
    public void deleteAllUsers() {
        jdbcTemplate.update("DELETE FROM users");
    }

    @Test
    void putAddUserToDataBase() {
        addUsersToDb();

        User user = new User(3, "user3@email.ru", "user3", "user 3", LocalDate.of(1995, 7, 31));

        Assertions.assertDoesNotThrow(() -> userStorage.put(user));

        Optional<User> wrappedUser = Assertions.assertDoesNotThrow(() -> userStorage.get(3));

        Assertions.assertTrue(wrappedUser.isPresent());
        Assertions.assertEquals(3, wrappedUser.get().getId());
    }

    @Test
    void putUpdateUser() {
        addUsersToDb();

        User user = new User(1, "user1@email.ru", "new_user1", "user 1", LocalDate.of(1980, 2, 3));

        Assertions.assertDoesNotThrow(() -> userStorage.put(user));

        Optional<User> wrappedUser = userStorage.get(1);

        Assertions.assertTrue(wrappedUser.isPresent());
        Assertions.assertEquals(1, wrappedUser.get().getId());
        Assertions.assertEquals("new_user1", wrappedUser.get().getLogin());
    }

    @Test
    void getReturnUserWhenUserIsFound() {
        addUsersToDb();

        Optional<User> wrappedUser = Assertions.assertDoesNotThrow(() -> userStorage.get(1));

        Assertions.assertTrue(wrappedUser.isPresent());
        Assertions.assertEquals(1, wrappedUser.get().getId());
        Assertions.assertEquals("name 1", wrappedUser.get().getName());
        Assertions.assertEquals(wrappedUser.get().getBirthday(), LocalDate.of(1980, 2, 3));
    }

    @Test
    void getReturnNullWhenUserIsNotFound() {
        Optional<User> wrappedUser = Assertions.assertDoesNotThrow(() -> userStorage.get(5));
        Assertions.assertTrue(wrappedUser.isEmpty());
    }

    @Test
    void getAllUsersReturnEmptyCollection() {
        Collection<User> users = Assertions.assertDoesNotThrow(userStorage::getAllUsers);
        Assertions.assertTrue(users.isEmpty());
    }

    @Test
    void getAllUsersReturnUsers() {
        addUsersToDb();

        Collection<User> users = Assertions.assertDoesNotThrow(userStorage::getAllUsers);
        Assertions.assertEquals(2, users.size());
    }

    @Test
    void isUserPresentReturnTrueWhenUserIsFound() {
        addUsersToDb();

        User user = new User(1, "user1@email.ru", "user1", "user 1", LocalDate.of(1980, 2, 3));
        boolean isUserPresent = Assertions.assertDoesNotThrow(() -> userStorage.isUserPresent(user));

        Assertions.assertTrue(isUserPresent);
    }

    @Test
    void isUserPresentReturnFalseWhenUserIsNotFound() {
        addUsersToDb();

        User user = new User(5, "user5@email.ru", "user5", "user 5", LocalDate.of(1980, 2, 3));
        boolean isUserPresent = Assertions.assertDoesNotThrow(() -> userStorage.isUserPresent(user));

        Assertions.assertFalse(isUserPresent);
    }
}