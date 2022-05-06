package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс исключения, которое может генерироваться при ошибке обновления пользователя или фильма.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UpdateException extends RuntimeException {
    public UpdateException(String msg) {
        super(msg);
    }
}
