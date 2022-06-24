package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Класс реализует интерфейс {@link FriendsStorage} и использует БД для хранения информации о друзьях.
 */
@Component("inDbFriendsStorage")
public class InDbFriendsStorage implements FriendsStorage {
    private static final String ADD_FRIEND =    "MERGE INTO friends (user_id, friend_id) " +
                                                "VALUES (?, ?)";

    private static final String DELETE_FRIEND = "DELETE FROM friends " +
                                                "WHERE user_id=? AND friend_id=?";

    private static final String REQUEST_FRIENDS =   "SELECT u.user_id AS u_id, " +
                                                    "       u.email AS email, " +
                                                    "       u.login AS login, " +
                                                    "       u.name AS name, " +
                                                    "       u.birthday AS birthday, " +
                                                    "FROM (SELECT friend_id " +
                                                    "      FROM friends " +
                                                    "      WHERE user_id=?) AS f " +
                                                    "LEFT JOIN users AS u ON f.friend_id=u.user_id";

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
     * Метод добавляет пользователя friend в список друзей пользователя user.
     *
     * @param user   пользователь, в список друзей которого, нужно добавить друга;
     * @param friend пользователь, которого нужно добавить в список друзей пользователя user;
     */
    @Override
    public void addFriend(User user, User friend) {
        friendsStorage.update(ADD_FRIEND, user.getId(), friend.getId());
    }

    /**
     * Метод удаляет пользователя friend из списка друзей поьзователя user.
     *
     * @param user  пользователь, из списка друзей которого нужно удалить друга;
     * @param friend    пользователь, которого нужно удалить из списка друзей пользователя user;
     */
    @Override
    public void deleteFriend(User user, User friend) {
        friendsStorage.update(DELETE_FRIEND, user.getId(), friend.getId());
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
        return friendsStorage.queryForStream(REQUEST_FRIENDS, (rs, num) -> new User(rs.getLong("u_id"),
                rs.getString("email"), rs.getString("login"), rs.getString("name"), rs.getDate("birthday")
                .toLocalDate()), user.getId()).collect(Collectors.toCollection(ArrayList::new));
    }
}