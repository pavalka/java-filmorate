package ru.yandex.practicum.filmorate.exception;

/**
 * Класс исключения, которое будет генерироваться в случае если данные для создания нового фильма или пользователя -
 * некорректны.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String msg) {
        super(msg);
    }
}
