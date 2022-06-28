package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class InDbLikeStorage implements LikesStorage {
    private static final String ADD_LIKE_TO_LIKES = "INSERT INTO likes VALUES (?, ?)";

    private static final String DELETE_LIKE_FROM_LIKES =    "DELETE FROM likes " +
                                                            "WHERE film_id=? AND user_id=?";

    private static final String INCREASE_LIKES_FOR_FILM =   "UPDATE films SET likes=likes+1 " +
                                                            "WHERE film_id=?";

    private static final String DECREASE_LIKES_FOR_FILM =   "UPDATE films SET likes=likes-1 " +
                                                            "WHERE film_id=?";

    private static final String REQUEST_USERS_BY_FILM_ID =  "SELECT u.user_id AS u_id, " +
                                                            "       u.email AS email, " +
                                                            "       u.login AS login, " +
                                                            "       u.name AS name, " +
                                                            "       u.birthday AS birthday " +
                                                            "FROM (SELECT user_id " +
                                                            "      FROM likes " +
                                                            "      WHERE film_id=?) AS l " +
                                                            "LEFT JOIN users AS u ON l.user_id=u.user_id";

    private final JdbcTemplate likeStorage;

    @Autowired
    public InDbLikeStorage(JdbcTemplate likeStorage) {
        this.likeStorage = likeStorage;
    }

    @Override
    public boolean addLike(Film film, User user) {
        if (film == null || user == null) {
            return false;
        }

        try {
            return (likeStorage.update(ADD_LIKE_TO_LIKES, film.getId(), user.getId()) != 0)
                    && (likeStorage.update(INCREASE_LIKES_FOR_FILM, film.getId()) != 0);
        } catch (DuplicateKeyException ex) {
            return false;
        }
    }

    @Override
    public boolean deleteLike(Film film, User user) {
        if (film == null || user == null) {
            return false;
        }

        return (likeStorage.update(DELETE_LIKE_FROM_LIKES, film.getId(), user.getId()) != 0)
                && (likeStorage.update(DECREASE_LIKES_FOR_FILM, film.getId()) != 0);
    }

    @Override
    public Collection<User> getUsersByFilmId(long filmId) {
        return likeStorage.queryForStream(REQUEST_USERS_BY_FILM_ID, (rs, num) -> {
            User user = new User(rs.getString("email"), rs.getString("login"), rs.getDate("birthday").toLocalDate());
            user.setId(rs.getLong("u_id"));
            user.setName(rs.getString("name"));
            return user;
        }, filmId).collect(Collectors.toCollection(ArrayList::new));
    }
}
