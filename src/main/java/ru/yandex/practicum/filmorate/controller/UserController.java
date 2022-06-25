package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.logger.UserControllerLogger;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserNotFoundException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserWithSameEmailException;

import java.util.Collection;

@RestController
public class UserController {
    private static final String USERS_PATH = "/users";

    private final UserService userService;
    private final Logger logger;

    @Autowired
    public UserController(UserService userService, UserControllerLogger userLogger) {
        this.userService = userService;
        this.logger = userLogger.getLogger();
    }

    /**
     * Метод возвращает список всех пользователей в хранилище.
     *
     * @return  список всех пользователей; если в хранилище нет пользователей, то метот вернет пустой список.
     */
    @GetMapping(USERS_PATH)
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Метод добавляет пользователя в хранилище.
     *
     * @param newUser   пользователь, которого нужно добавить;
     * @return  пользователя с присвоенным ему идентификатором;
     * @throws ValidationException  генерируется если пользователь не удовлетворяет заданным условиям;
     * @throws UserWithSameEmailException   генерируется если в хранилище есть пользователь с email = user.email;
     */
    @PostMapping(USERS_PATH)
    public User addUser(@RequestBody User newUser) {
        newUser = userService.addUser(newUser);
        logger.info("addUser: создан новый пользователь с id = {}", newUser.getId());
        return newUser;
    }

    /**
     * Метод обновляет пользователя newUser в хранилище.
     *
     * @param newUser   пользователь, которого нужно обновить;
     * @return  пользователь;
     * @throws ValidationException  генерируется если пользователь не удовлетворяет заданным условиям;
     * @throws UserNotFoundException    генерируется если пользователь newUser не найден в хранилище;
     */
    @PutMapping(USERS_PATH)
    public User updateUser(@RequestBody User newUser) {
        userService.updateUser(newUser);
        logger.info("updateUser: обновлен пользователь с id = {}", newUser.getId());
        return newUser;
    }

    /**
     * Метод возвращает пользователя с идентификатором userId.
     *
     * @param userId    идентификатор пользователя;
     * @return  пользователь с идентификатором userId;
     * @throws UserNotFoundException    генерируется если пользователь не найден в хранилище;
     */
    @GetMapping(USERS_PATH + "/{id}")
    public User getUser(@PathVariable("id") long userId) {
        return userService.getUser(userId);
    }

    /**
     * Метод добавляет пользователя с идентификатором friendId в список друзей пользователя с идентификатором userId.
     *
     * @param userId   идентификатор пользователя, которому нужно добавить друга;
     * @param friendId  идентификатор пользователя, который должен быть добавлен как друг;
     * @throws UserNotFoundException    генерируется если один из пользователей с идентификаторами friendId или userId
     *                                  не найден в хранилище.
     */
    @PutMapping(USERS_PATH + "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long userId, @PathVariable long friendId) {
        userService.addFriend(userId, friendId);
        logger.info("addFriend: пользователь с id = {} добавлен в друзья пользователя с id = {}", friendId, userId);
    }

    /**
     * Метод удаляет пользователя с идентификатором friendId из списка друзей пользователя с идентификатором userId.
     *
     * @param userId    идентификатор пользователя, у которого нужно удалить друга;
     * @param friendId  идентификатор пользователя, который должен быть удален как друг;
     * @throws UserNotFoundException    генерируется если один из пользователей с идентификаторами friendId или userId
     *                                  не найден в хранилище.
     */
    @DeleteMapping(USERS_PATH + "/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") long userId, @PathVariable long friendId) {
        userService.deleteFriend(userId, friendId);
        logger.info("deleteFriend: пользователь с id = {} удален из друзей пользователя с id = {}", friendId, userId);
    }

    /**
     * Метод возвращает список общих друзей пользователей с идентификаторами userIdOne и userIdTwo.
     *
     * @param userIdOne идентификатор первого пользователя;
     * @param userIdTwo идентификатор второго пользователя;
     * @return  список общих друзей; если общих друзей у пользователей с идентификаторами userIdOne и userIdTwo нет, то
     *          метод вернет пустой список;
     * @throws UserNotFoundException    генерируется если один из пользователей с идентификаторами friendId или userId
     *                                  не найден в хранилище.
     */
    @GetMapping(USERS_PATH + "/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") long userIdOne, @PathVariable("otherId") long userIdTwo) {
        return userService.getCommonFriends(userIdOne, userIdTwo);
    }

    /**
     * Метод возвращает список друзей для пользователя с идентификатором userId.
     *
     * @param userId    идентификатор пользователя, для которго нужно получить список друзей;
     * @return  список друзей; если у пользователя с идентификатором userId нет друзей, то метод вернет пустой список;
     * @throws UserNotFoundException    генерируется если один из пользователей с идентификаторами friendId или userId
     *                                  не найден в хранилище.
     */
    @GetMapping(USERS_PATH + "/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable("id") long userId) {
        return userService.getUserFriends(userId);
    }
}
