package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;

/**
 * Класс исключения, которое может быть сшенерировано если запрашивемый пользователь не найден в хранилище.
 */
public class UserNotFoundException extends ElementNotFoundException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
