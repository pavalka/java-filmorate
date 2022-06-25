package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс реализует интерфейс UserStorage и использует БД для хранения пользователей.
 */
@Component("inDbUserStorage")
public class InDbUserStorage implements UserStorage {
    private final static String REQUEST_MAX_USER_ID = "SELECT MAX(user_id) AS max_id FROM users";

    private final static String ADD_NEW_USER =  "MERGE INTO users (user_id, email, login, name, birthday) " +
                                                "VALUES (?, ?, ?, ?, ?)";

    private final static String REQUEST_USER_BY_ID =    "SELECT user_id, email, login, name, birthday " +
                                                        "FROM users " +
                                                        "WHERE user_id=?";

    private final static String REQUEST_ALL_USERS = "SELECT * FROM users";

    private final static String REQUEST_USER_ID_BY_EMAIL =  "SELECT user_id FROM users " +
                                                            "WHERE email=?";

    private final JdbcTemplate userStorage;

    /**
     * Конструктор класса.
     *
     * @param jdbcTemplate  объект класса {@link JdbcTemplate}.
     */
    @Autowired
    public InDbUserStorage(JdbcTemplate jdbcTemplate) {
        userStorage = jdbcTemplate;
    }

    /**
     * Метод добавляет item в хранилище.
     *
     * @param item объект для добавления в хранилище;
     */
    @Override
    public void put(User item) {
        userStorage.update(ADD_NEW_USER, item.getId(), item.getEmail(), item.getLogin(), item.getName(),
                           item.getBirthday());
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
    public Optional<User> get(long key) {
        return Optional.ofNullable(userStorage.query(REQUEST_USER_BY_ID, rs -> {
            if (rs.next()) {
                var user =new User(rs.getString("email"), rs.getString("login"),
                              rs.getDate("birthday").toLocalDate());
                user.setId(rs.getLong("user_id"));
                user.setName(rs.getString("name"));
                return user;
            }

            return null;
        }, key));
    }

    /**
     * Метод возвращает список всех пользователей, находящихся в хранилище в виде Collection<>. Если в хранилище нет ни
     * одного пользователя, то метод вернет пустой список.
     *
     * @return список всех пользователей из хранилища; если в хранилище нет ни одного пользователя, то метод вернет
     * пустой список.
     */
    @Override
    public Collection<User> getAllUsers() {
        return userStorage.queryForStream(REQUEST_ALL_USERS, (rs, num) -> {
            var user= new User (rs.getString("email"), rs.getString("login"),
                      rs.getDate("birthday").toLocalDate());

            user.setId(rs.getLong("user_id"));
            user.setName(rs.getString("name"));
            return user;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Метод проверяет присутствует ли в хранилище пользователь user. Если этот пользователь есть в хранилище
     * пользователей, то метод вернет true. В противном случает метод вернет false.
     *
     * @param user пользователь, которого нужно проверить;
     * @return если этот пользователь есть в хранилище пользователей, то метод вернет true; в противном случает метод
     * вернет false.
     */
    @Override
    public boolean isUserPresent(User user) {
        Long userId = userStorage.query(REQUEST_USER_ID_BY_EMAIL, rs -> {
            if (rs.next()) {
                return rs.getLong("user_id");
            }
            return null;
            }, user.getEmail());

        return userId != null;
    }

    /**
     * Метод возвращает наибольшее значение идентификатора пользователя из БД.
     *
     * @return  максимальное значение идентификатора пользователя.
     */
    public Optional<Long> getMaxUserId() {
        return Optional.ofNullable(userStorage.query(REQUEST_MAX_USER_ID, rs -> {
            if (rs.next()) {
                return rs.getLong("max_id");
            }
            return null;
            }));
    }
}
