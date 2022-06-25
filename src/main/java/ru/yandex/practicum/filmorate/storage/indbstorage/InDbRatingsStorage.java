package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.RatingsStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Класс реализует интерфейс RatingsStorage и использует БД для хранения списка рейтингов.
 */
@Component
@Profile("in_db_storage")
public class InDbRatingsStorage implements RatingsStorage {
    private static final String REQUEST_ALL_RATINGS = "SELECT * FROM ratings";

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
}
