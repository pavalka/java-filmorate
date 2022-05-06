package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UpdateException;
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
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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
        if (newUser == null || !UserValidator.validate(newUser)) {
            logger.warn("addUser: запрос не соответствует условиям. user = {}", newUser);
            throw new ValidationException("Параметры пользователя не соответствуют заданным условиям");
        }

        newUser.setId(IdGenerator.getNextId());
        users.put(newUser.getId(), newUser);
        logger.info("addUser: создан новый пользователь с id = {}", newUser.getId());
    }

    @PutMapping(USERS_PATH)
    public void updateUser(@RequestBody User newUser) throws ValidationException {
        if (newUser == null) {
            logger.warn("updateUser: запрос не соответствует условиям. user = null");
            throw new ValidationException("Пустой запрос");
        }
        if (users.containsKey(newUser.getId())) {
            users.put(newUser.getId(), newUser);
            logger.info("updateUser: обновлен пользователь с id = {}", newUser.getId());
        } else {
            logger.warn("updateUser: пользователь не найден. user = {}", newUser);
            throw new UpdateException("Неверный id пользователя");
        }
    }
}
