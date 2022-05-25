package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

/**
 * Определяет функционал хранилища пользователей.
 */
public interface UserStorage extends Storage<User, Long> {
    /**
     * Метод возвращает список всех пользователей, находящихся в хранилище в виде Collection<>. Если в хранилище нет ни
     * одного пользователя, то метод вернет пустой список.
     *
     * @return  список всех пользователей из хранилища; если в хранилище нет ни одного пользователя, то метод вернет
     *          пустой список.
     */
    Collection<User> getAllUsers();
}
