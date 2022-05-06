package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    public void runBeforeEachTest() {
        userController = new UserController();
    }

    @Test
    void getAllUsersShouldReturnEmptyCollectionWhenNoUsersAreAdded() {
        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void getAllUserShouldReturnUserCollectionWhenOneUserIsAdded() {
        User user = new User("test@email.ru", "user_login", "user_name", LocalDate.of(1990,
                      3, 12));
        assertDoesNotThrow(() -> userController.addUser(user));
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void addUserShouldThrowExceptionWhenAddNull() {
        assertThrows(ValidationException.class, () -> userController.addUser(null));
    }

    @Test
    void addUserShouldThrowExceptionWhenAddUserWithEmptyEmail() {
        User user = new User("", "user_login", "user_name", LocalDate.of(1990, 3,
                  12));
        assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void addUserShouldThrowExceptionWhenAddUserWithWrongEmail() {
        User user = new User("user_email.ru", "user_login", "user_name", LocalDate.of(1990, 3,
                12));
        assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void addUserShouldThrowExceptionWhenAddUserWithEmptyLogin() {
        User user = new User("user@email.ru", "", "user_name", LocalDate.of(1990, 3,
                  12));
        assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void addUserShouldThrowExceptionWhenAddUserWithWrongLogin() {
        User user = new User("user@email.ru", "user login", "user_name", LocalDate.of(1990,
                      3, 12));
        assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void addUserShouldAddUserWhenUserNameIsEmpty() {
        User user = new User("user@email.ru", "user_login", "", LocalDate.of(1990, 3,
                  12));
        assertDoesNotThrow(() -> userController.addUser(user));
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void addUserShouldThrowExceptionWhenUserBirthdateIsAfterNow() {
        User user = new User("user@email.ru", "user_login", "user_name", LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void addUserShouldAddUser() {
        User user = new User("user@email.ru", "user_login", "user_name", LocalDate.of(1990,
                             3, 12));
        assertDoesNotThrow(() -> userController.addUser(user));
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void updateUserShouldThrowExceptionWhenUpdateNull() {
        assertThrows(ValidationException.class, () -> userController.updateUser(null));
    }

    @Test
    void updateUserShouldThrowExceptionWhenUserIdIsWrong() {
        User user = new User("user@email.ru", "user_login", "user_name", LocalDate.of(1990,
                             3, 12));
        assertDoesNotThrow(() -> userController.addUser(user));
        user.setId(100000);
        assertThrows(UpdateException.class, () -> userController.updateUser(user));
    }

    @Test
    void updateUserShouldUpdateUser() {
        User user = new User("user@email.ru", "user_login", "user_name", LocalDate.of(1990,
                3, 12));

        assertDoesNotThrow(() -> userController.addUser(user));

        User newUser = new User("user@email.ru", "user_login", "new_user_name", LocalDate.of(1990,
                3, 12));

        newUser.setId(user.getId());
        assertDoesNotThrow(() -> userController.updateUser(newUser));
        for (User currentUser : userController.getAllUsers()) {
            assertEquals(newUser.getId(), currentUser.getId());
            assertEquals(newUser.getEmail(), currentUser.getEmail());
            assertEquals(newUser.getLogin(), currentUser.getLogin());
            assertEquals(newUser.getName(), currentUser.getName());
            assertEquals(newUser.getBirthday(), currentUser.getBirthday());
        }
    }
}