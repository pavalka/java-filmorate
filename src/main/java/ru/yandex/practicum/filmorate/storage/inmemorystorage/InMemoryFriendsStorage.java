package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс реализует функционал хранилища списка друзей пользователя.
 */
@Profile("in_memory_storage")
@Component
public class InMemoryFriendsStorage implements FriendsStorage {
    private final Map<Long, Set<Long>> friendsStorage;
    private final UserStorage userStorage;

    /**
     * Конструктор класса.
     *
     * @param userStorage   хранилище пользователей.
     */
    @Autowired
    public InMemoryFriendsStorage(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = new HashMap<>();
    }

    /**
     * Метод добавляет пользователя friend в список друзей пользователя user.
     *
     * @param user   пользователь, в список друзей которого, нужно добавить друга;
     * @param friend пользователь, которого нужно добавить в список друзей пользователя user;
     */
    @Override
    public void addFriend(User user, User friend) {
        Set<Long> friends = friendsStorage.getOrDefault(user.getId(), new HashSet<>());
        friends.add(friend.getId());
        friendsStorage.put(user.getId(), friends);
    }

    /**
     * Метод удаляет пользователя friend из списка друзей поьзователя user.
     *
     * @param user  пользователь, из списка друзей которого нужно удалить друга;
     * @param friend    пользователь, которого нужно удалить из списка друзей пользователя user;
     */
    @Override
    public void deleteFriend(User user, User friend) {
        Set<Long> friends = friendsStorage.get(user.getId());

        if (friends != null) {
            friends.remove(friend.getId());
            friendsStorage.put(user.getId(), friends);
        }
    }

    /**
     * Метод возвращает список друзей пользователя user. Если у пользователя нет ни одного друга, то метод вернет
     * пустой список.
     *
     * @param user пользователь, список друзей которого нужно получить;
     * @return список друзей пользователя user;
     * пустой список, если у пользователя нет ни одного друга.
     */
    @Override
    public Collection<User> getFriends(User user) {
        Set<Long> friends = friendsStorage.getOrDefault(user.getId(), new HashSet<>());

        return friends.stream().map((friendId) -> userStorage.get(friendId).get())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
