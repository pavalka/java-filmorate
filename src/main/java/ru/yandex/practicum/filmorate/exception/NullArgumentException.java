package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Генерируется, если аргумент метода равен null.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NullArgumentException extends IllegalArgumentException {
    public NullArgumentException(String msg) {
        super(msg);
    }
}
