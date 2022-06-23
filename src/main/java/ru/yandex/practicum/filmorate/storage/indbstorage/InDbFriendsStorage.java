package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.Optional;

/**
 * Класс реализует интерфейс {@link FriendsStorage} и использует БД для хранения информации о друзьях.
 */
@Component("inDbFriendsStorage")
public class InDbFriendsStorage implements FriendsStorage {
    private static final String ADD_FRIEND =    "MERGE INTO friends (user_id, friend_id) " +
                                                "VALUES (?, ?)";

    private static final String DELETE_FRIEND = "DELETE FROM friends " +
                                                "WHERE user_id=? AND friend_id=?";

    private static final String REQUEST_FRIENDS =   "SELECT friend_id " +
                                                    "FROM friends " +
                                                    "WHERE user_id=?";

    private final JdbcTemplate friendsStorage;

    /**
     * Конструктор класса.
     *
     * @param friendsStorage    объект класса {@link JdbcTemplate}.
     */
    @Autowired
    public InDbFriendsStorage(JdbcTemplate friendsStorage) {
        this.friendsStorage = friendsStorage;
    }

    /**
     * Метод добавляет пользователя с идентификатором friendId в список друзей пользователя с идентификатором userId.
     *
     * @param userId   идентификатор пользователя в список друзей которого, нужно добавить друга;
     * @param friendId идентификатор пользователя, которго добавляем в список друзей;
     */
    @Override
    public void addFriend(long userId, long friendId) {
        friendsStorage.update(ADD_FRIEND, userId, friendId);
    }

    /**
     * Метод удаляет пользователя с идентификатором friendId из списка друзей поьзователя с идентификатором userId.
     *
     * @param userId   идентификатор пользователя из списка друзей которого, нужно удалить друга
     * @param friendId идентификатор пользователя, которого нужно удалить из списка друзей;
     */
    @Override
    public void deleteFriend(long userId, long friendId) {
        friendsStorage.update(DELETE_FRIEND, userId, friendId);
    }

    /**
     * Метод добавляет item в хранилище.
     *
     * @param item объект для добавления в хранилище;
     */
    @Override
    public void put(Friends item) {
        for (long friendId : item.getFriendsId()) {
            addFriend(item.getId(), friendId);
        }
    }

    /**
     * Метод возвращает из хранилища объект с ключом key, обернутый в класс {@link Optional}. Если объект с
     * ключом key не найден в хранилище, то в {@link Optional} будет помещен null.
     *
     * @param key ключ объекта, который мы хотим получить;
     * @return объект с ключом key, обернутый в класс {@link Optional}.
     * Если объект с ключом key не найден в хранилище, то в {@link Optional} будет помещен null
     */
    @Override
    public Optional<Friends> get(long key) {
        Friends friends = friendsStorage.query(REQUEST_FRIENDS, rs -> {
            if (!rs.next()) {
                return null;
            }

            Friends friendList = new Friends(key);

            do {
                friendList.addFriend(rs.getLong("friend_id"));
            } while (rs.next());

            return friendList;
        }, key);
        return Optional.ofNullable(friends);
    }
}