package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserNotFoundException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.inmemorystorage.InMemoryFriendsStorage;
import ru.yandex.practicum.filmorate.storage.inmemorystorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UserControllerTest {

    private UserService userService;

    @BeforeEach
    public void runBeforeEachTest() {
        UserStorage userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage, new InMemoryFriendsStorage(userStorage), new IdGenerator());
    }

    @Test
    void getAllUsersShouldReturnEmptyCollectionWhenNoUsersAreAdded() {
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    void getAllUserShouldReturnUserCollectionWhenOneUserIsAdded() {
        User user = new User("test@email.ru", "user_login", "user_name", LocalDate.of(1990, 3, 12));
        assertDoesNotThrow(() -> userService.addUser(user));
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void addUserShouldThrowExceptionWhenAddNull() {
        assertThrows(ValidationException.class, () -> userService.addUser(null));
    }

    @Test
    void addUserShouldThrowExceptionWhenAddUserWithEmptyEmail() {
        User user = new User("", "user_login", "user_name", LocalDate.of(1990, 3, 12));
        assertThrows(ValidationException.class, () -> userService.addUser(user));
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    void addUserShouldThrowExceptionWhenAddUserWithWrongEmail() {
        User user = new User("user_email.ru", "user_login", "user_name", LocalDate.of(1990, 3, 12));
        assertThrows(ValidationException.class, () -> userService.addUser(user));
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    void addUserShouldThrowExceptionWhenAddUserWithEmptyLogin() {
        User user = new User("user@email.ru", "", "user_name", LocalDate.of(1990, 3, 12));
        assertThrows(ValidationException.class, () -> userService.addUser(user));
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    void addUserShouldThrowExceptionWhenAddUserWithWrongLogin() {
        User user = new User("user@email.ru", "user login", "user_name", LocalDate.of(1990, 3, 12));
        assertThrows(ValidationException.class, () -> userService.addUser(user));
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    void addUserShouldAddUserWhenUserNameIsEmpty() {
        User user = new User("user@email.ru", "user_login", "", LocalDate.of(1990, 3, 12));
        assertDoesNotThrow(() -> userService.addUser(user));
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void addUserShouldThrowExceptionWhenUserBirthdateIsAfterNow() {
        User user = new User("user@email.ru", "user_login", "user_name", LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userService.addUser(user));
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    void addUserShouldAddUser() {
        User user = new User("user@email.ru", "user_login", "user_name", LocalDate.of(1990, 3, 12));
        assertDoesNotThrow(() -> userService.addUser(user));
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void updateUserShouldThrowExceptionWhenUpdateNull() {
        assertThrows(ValidationException.class, () -> userService.updateUser(null));
    }

    @Test
    void updateUserShouldThrowExceptionWhenUserIdIsWrong() {
        User user = new User("user@email.ru", "user_login", "user_name", LocalDate.of(1990, 3, 12));
        assertDoesNotThrow(() -> userService.addUser(user));
        user.setId(100000);
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));
    }

    @Test
    void updateUserShouldUpdateUser() {
        User user = new User("user@email.ru", "user_login", "user_name", LocalDate.of(1990, 3, 12));

        assertDoesNotThrow(() -> userService.addUser(user));

        User newUser = new User("user@email.ru", "user_login", "new_user_name", LocalDate.of(1990, 3, 12));

        newUser.setId(user.getId());
        assertDoesNotThrow(() -> userService.updateUser(newUser));
        for (User currentUser : userService.getAllUsers()) {
            assertEquals(newUser.getId(), currentUser.getId());
            assertEquals(newUser.getEmail(), currentUser.getEmail());
            assertEquals(newUser.getLogin(), currentUser.getLogin());
            assertEquals(newUser.getName(), currentUser.getName());
            assertEquals(newUser.getBirthday(), currentUser.getBirthday());
        }
    }
}