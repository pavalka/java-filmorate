package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;

/**
 * Класс исключения, которое может быть сгенерировано если запрашивемый жанр не найден в хранилище.
 */
public class GenreNotFoundException extends ElementNotFoundException {
    public GenreNotFoundException(String msg) {
        super(msg);
    }
}
