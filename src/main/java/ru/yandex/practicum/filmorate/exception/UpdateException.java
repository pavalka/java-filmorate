package ru.yandex.practicum.filmorate.exception;

/**
 * Класс исключения, которое может генерироваться при ошибке обновления пользователя или фильма.
 */
public class UpdateException extends RuntimeException {
    public UpdateException(String msg) {
        super(msg);
    }
}
