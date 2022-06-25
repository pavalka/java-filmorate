package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Класс реализует интерфейс {@link GenresStorage} использует БД для храниния жанров.
 */
@Component
@Profile("in_db_storage")
public class InDbGenresStorage implements GenresStorage {
    private final JdbcTemplate genresStorage;

    private static final String REQUEST_ALL_GENRES =    "SELECT * FROM genres";
    /**
     * Конструктор класса.
     *
     * @param genresStorage объект класса {@link JdbcTemplate}
     */
    @Autowired
    public InDbGenresStorage(JdbcTemplate genresStorage) {
        this.genresStorage = genresStorage;
    }

    /**
     * Метод возвращает список всех жанроов в хранилище. Если в хранилище нет ни одного жанра, то метод вернет пустой
     * список.
     *
     * @return список всех жанроов в хранилище; пустой список, если в хранилище нет ни одного жанра;
     */
    @Override
    public Collection<Genre> getAllGenres() {
        return genresStorage.queryForStream(REQUEST_ALL_GENRES, (rs, num) -> new Genre(rs.getInt("genre_id"),
                rs.getString("name"))).collect(Collectors.toCollection(ArrayList::new));
    }
}
