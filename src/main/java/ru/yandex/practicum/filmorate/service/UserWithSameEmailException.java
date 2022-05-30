package ru.yandex.practicum.filmorate.service;

/**
 * Класс исключения, которое может быть сгенерировано если в хранилище пользователей пытаются добавить пользователя,
 * email которого совпадает с email другого пользователя из хранилища.
 */
public class UserWithSameEmailException extends RuntimeException {
    public UserWithSameEmailException(String msg) {
        super(msg);
    }
}
