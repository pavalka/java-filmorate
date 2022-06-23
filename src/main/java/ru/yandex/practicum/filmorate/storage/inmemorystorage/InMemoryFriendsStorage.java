package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

/**
 * Класс реализует функционал хранилища списка друзей пользователя.
 */
@Component("inMemoryFriendsStorage")
public class InMemoryFriendsStorage extends AbstractInMemoryStorage<Friends> implements FriendsStorage {
    /**
     * Метод добавляет пользователя с идентификатором friendId в список друзей пользователя с идентификатором userId.
     *
     * @param userId   идентификатор пользователя в список друзей которого, нужно добавить друга;
     * @param friendId идентификатор пользователя, которго добавляем в список друзей;
     */
    @Override
    public void addFriend(long userId, long friendId) {
        Friends friends = storage.getOrDefault(userId, new Friends(userId));
        friends.addFriend(friendId);
        put(friends);
    }

    /**
     * Метод удаляет пользователя с идентификатором friendId из списка друзей поьзователя с идентификатором userId.
     *
     * @param userId   идентификатор пользователя из списка друзей которого, нужно удалить друга
     * @param friendId идентификатор пользователя, которого нужно удалить из списка друзей;
     */
    @Override
    public void deleteFriend(long userId, long friendId) {
        Friends friends = storage.get(userId);

        if (friends != null) {
            friends.deleteFriend(friendId);
            put(friends);
        }
    }
}
