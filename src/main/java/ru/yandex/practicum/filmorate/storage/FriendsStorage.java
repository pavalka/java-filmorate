package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friends;

/**
 * Определяет функционал хранилища списка друзей пользователя.
 */
public interface FriendsStorage extends Storage<Friends> {
    /**
     * Метод добавляет пользователя с идентификатором friendId в список друзей пользователя с идентификатором userId.
     *
     * @param userId   идентификатор пользователя в список друзей которого, нужно добавить друга;
     * @param friendId  идентификатор пользователя, которго добавляем в список друзей;
     */
    void addFriend(long userId, long friendId);

    /**
     * Метод удаляет пользователя с идентификатором friendId из списка друзей поьзователя с идентификатором userId.
     *
     * @param userId    идентификатор пользователя из списка друзей которого, нужно удалить друга
     * @param friendId  идентификатор пользователя, которого нужно удалить из списка друзей;
     */
    void deleteFriend(long userId, long friendId);
}
