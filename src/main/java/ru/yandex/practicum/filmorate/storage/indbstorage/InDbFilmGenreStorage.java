package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс реализует интерфейс FilmGenreStorage и использует БД для хранения данных.
 */
@Component
public class InDbFilmGenreStorage implements FilmGenreStorage {
    private static final String REQUEST_GENRES_FOR_FILM =   "SELECT g.genre_id AS id, " +
                                                            "       g.name AS name " +
                                                            "FROM (SELECT * FROM film_genre " +
                                                            "      WHERE film_id=?) AS fg " +
                                                            "LEFT JOIN genres AS g ON fg.genre_id=g.genre_id";

    private static final String REQUEST_GENRES_FOR_ALL_FILMS =  "SELECT fg.film_id AS f_id, " +
                                                                "       g.genre_id AS id, " +
                                                                "       g.name AS name " +
                                                                "FROM film_genre AS fg " +
                                                                "LEFT JOIN genres AS g ON fg.genre_id=g.genre_id";

    private static final String DELETE_GENRES_FOR_FILM = "DELETE FROM film_genre WHERE film_id=?";

    private static final String ADD_GENRE_FOR_FILM = "INSERT INTO film_genre VALUES (?, ?)";

    private final JdbcTemplate filmGenreStorage;

    /**
     * Конструктор класса.
     *
     * @param jdbcTemplate  объект класса {@link JdbcTemplate};
     */
    @Autowired
    public InDbFilmGenreStorage(JdbcTemplate jdbcTemplate) {
        filmGenreStorage = jdbcTemplate;
    }

    /**
     * Метод возвращает набор жанров для заданного фильма. Если для данного фильма не задан ни один жанр, то метод
     * вернет пустой набор. Если в процессе работы произойдет ошибка, то метод сгенерирует исключение
     * {@link DataAccessException}.
     *
     * @param filmId идентификатор фильма;
     * @return набор жанров для заданного фильма; если для данного фильма не задан ни один жанр, то метод вернет пустой
     * набор.
     * @throws DataAccessException будет сгенерировано если в процессе работы произойдет
     *                             ошибка.
     */
    @Override
    public Set<Genre> getGenresForFilmByFilmId(long filmId) {
        return filmGenreStorage.queryForStream(REQUEST_GENRES_FOR_FILM, (rs, num) -> new Genre(rs.getInt("id"),
                rs.getString("name")), filmId).collect(Collectors.toSet());
    }

    /**
     * Метод возвращает наборы жанров для всех фильмов в виде объекта класса Map. В качетсве ключа в Map выступает id
     * фильма. Если таблица film_genre пуста, то метод врент пустой объек класса Map. Если в процессе работы метода
     * произойдет ошибка, то будет сгенерировано исключение {@link DataAccessException}.
     *
     * @return наборы жанров для всех фильмов в виде объекта класса Map; если таблица film_genre пуста, то метод врент
     * пустой объек класса Map.
     * @throws DataAccessException будет сгенерировано если в процессе работы метода произойдет
     *                             ошибка.
     */
    @Override
    public Map<Long, Set<Genre>> getGenresForAllFilms() {
        Map<Long, Set<Genre>> genresForAllFilms = new HashMap<>();
        filmGenreStorage.query(REQUEST_GENRES_FOR_ALL_FILMS, rs -> {
            Long filmId = rs.getLong("f_id");
            Set<Genre> currentGenres = genresForAllFilms.getOrDefault(filmId, new HashSet<>());
            currentGenres.add(new Genre(rs.getInt("id"), rs.getString("name")));
            genresForAllFilms.put(filmId, currentGenres);
        });

        return genresForAllFilms;
    }

    /**
     * Метод добавляет/обновляет список жанров genres для фильма с идентификатором filmId. Если в процессе работы метода
     * произойдет ошибка, то будет сгенерировано исключение {@link DataAccessException}.
     *
     * @param filmId идентификатор фильма;
     * @param genres список жанров для фильма;
     * @throws DataAccessException генерируется, если в процессе работы метода произойдет ошибка
     */
    @Override
    public void putGenresForFilm(long filmId, Set<Genre> genres) {
        filmGenreStorage.update(DELETE_GENRES_FOR_FILM, filmId);

        if (genres != null && !genres.isEmpty()) {
            for (Genre currentGenre : genres) {
                filmGenreStorage.update(ADD_GENRE_FOR_FILM, filmId, currentGenre.getId());
            }
        }
    }
}
