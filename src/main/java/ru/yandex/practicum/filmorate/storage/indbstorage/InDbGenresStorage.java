package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс реализует интерфейс {@link GenresStorage} использует БД для храниния жанров.
 */
@Component
@Profile("in_db_storage")
public class InDbGenresStorage implements GenresStorage {
    private final JdbcTemplate genresStorage;

    private static final String REQUEST_ALL_GENRES =    "SELECT * FROM genres";

    private static final String REQUEST_GENRE_BY_ID =   "SELECT * FROM genres " +
                                                        "WHERE genre_id=?";
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

    /**
     * Метод возвращает жанр с идентификатором genreId. Если жанр с таким идентификатором не найден метод вренет пустой
     * Optional.
     *
     * @param genreId идентификатор жанра;
     * @return жанр с заданным идентификатором; пустой объект Optional, если жанр с ижентификатором genreId не найден.
     */
    @Override
    public Optional<Genre> getGenreById(int genreId) {
        return Optional.ofNullable(genresStorage.query(REQUEST_GENRE_BY_ID, rs-> {
            if (rs.next()) {
                return new Genre(rs.getInt("genre_id"), rs.getString("name"));
            }

            return null;
        }, genreId));
    }
}
