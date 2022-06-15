package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;

/**
 * Класс исключения, которое может быть сгенерировано если запрашиваемый фильм не найден в хранилище.
 */
public class FilmNotFoundException extends ElementNotFoundException {
    public FilmNotFoundException(String msg) {
        super(msg);
    }
}
