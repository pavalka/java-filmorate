package ru.yandex.practicum.filmorate.exception;

/**
 * Генерируется, если аргумент метода равен null.
 */
public class NullArgumentException extends IllegalArgumentException {
    public NullArgumentException(String msg) {
        super(msg);
    }
}
