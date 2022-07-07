package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

/**
 * Определяет функционал хранилища списка друзей пользователя.
 */
public interface FriendsStorage {
    /**
     * Метод добавляет пользователя friend в список друзей пользователя user.
     *
     * @param user   пользователь, в список друзей которого, нужно добавить друга;
     * @param friend пользователь, которого нужно добавить в список друзей пользователя user;
     */
    void addFriend(User user, User friend);

    /**
     * Метод удаляет пользователя friend из списка друзей поьзователя user.
     *
     * @param user  пользователь, из списка друзей которого нужно удалить друга;
     * @param friend    пользователь, которого нужно удалить из списка друзей пользователя user;
     */
    void deleteFriend(User user, User friend);

    /**
     * Метод возвращает список друзей пользователя user. Если у пользователя нет ни одного друга, то метод вернет
     * пустой список.
     *
     * @param user  пользователь, список друзей которого нужно получить;
     * @return  список друзей пользователя user;
     *          пустой список, если у пользователя нет ни одного друга.
     */
    Collection<User> getFriends(User user);
}
