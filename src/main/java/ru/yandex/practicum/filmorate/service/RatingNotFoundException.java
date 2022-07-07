package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;

/**
 * Класс исключения, которое может быть сшенерировано если запрашивемый рейтинг не найден в хранилище.
 */
public class RatingNotFoundException extends ElementNotFoundException {
    public RatingNotFoundException(String msg) {
        super(msg);
    }
}
