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

    /**
     * Метод проверяет присутствует ли в хранилище пользователь user. Если этот пользователь есть в хранилище
     * пользователей, то метод вернет true. В противном случает метод вернет false.
     *
     * @param user  пользователь, которого нужно проверить;
     * @return  если этот пользователь есть в хранилище пользователей, то метод вернет true; в противном случает метод
     *          вернет false.
     */
    boolean isUserPresent(User user);
}
