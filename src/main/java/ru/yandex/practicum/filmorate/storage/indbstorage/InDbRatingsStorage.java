package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.RatingsStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс реализует интерфейс RatingsStorage и использует БД для хранения списка рейтингов.
 */
@Component
public class InDbRatingsStorage implements RatingsStorage {
    private static final String REQUEST_ALL_RATINGS = "SELECT * FROM ratings";

    private static final String REQUEST_RATING_BY_ID =  "SELECT * FROM ratings " +
                                                        "WHERE rating_id=?";

    private final JdbcTemplate ratingsStorage;

    /**
     * Конструктор класса.
     *
     * @param ratingsStorage    объект класса {@link JdbcTemplate}.
     */
    @Autowired
    public InDbRatingsStorage(JdbcTemplate ratingsStorage) {
        this.ratingsStorage = ratingsStorage;
    }

    /**
     * Метод возвращает список всех ретингов в хранилище. Если в хранилище нет ни одного рейтинга, то метод вернет
     * пустой список.
     *
     * @return список всех ретингов в хранилище; пустой список, если в хранилище нет ни одного рейтинга.
     */
    @Override
    public Collection<Mpa> getAllRatings() {
        return ratingsStorage.queryForStream(REQUEST_ALL_RATINGS, (rs, num) -> new Mpa(rs.getInt("rating_id"),
                rs.getString("name"))).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Метод возвращает райтинг фильма с идентификатором ratingId. Если рейтинг с таким идентификатором не найден, то
     * метод вернет пустой Optional.
     *
     * @param ratingId идентификатор рейтинга фильма;
     * @return райтинг фильма; пустой объект {@link Optional}, если рейтинг с таким идентификатором не найден.
     */
    @Override
    public Optional<Mpa> getRatingById(int ratingId) {
        return Optional.ofNullable(ratingsStorage.query(REQUEST_RATING_BY_ID, rs -> {
            if (rs.next()) {
                return new Mpa(rs.getInt("rating_id"), rs.getString("name"));
            }

            return null;
        }, ratingId));
    }
}
