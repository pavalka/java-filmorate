package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.IdGenerator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private static final String USERS_PATH = "/users";

    private final Map<Long, User> users;

    public UserController() {
        this.users = new HashMap<>();
    }

    @GetMapping(USERS_PATH)
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping(USERS_PATH)
    public void addUser(@RequestBody User newUser) throws ValidationException {
        if (!UserValidator.validate(newUser)) {
            throw new ValidationException("Параметры пользователя не соответствуют заданным условиям");
        }

        newUser.setId(IdGenerator.getNextId());
        users.put(newUser.getId(), newUser);
    }

    @PutMapping(USERS_PATH)
    public void updateUser(@RequestBody User newUser) {
        users.put(newUser.getId(), newUser);
    }
}
