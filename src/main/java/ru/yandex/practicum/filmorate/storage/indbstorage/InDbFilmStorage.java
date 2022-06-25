package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullArgumentException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс реализует интерфейс FilmStorage и использует БД в качестве кранилища фильмов.
 */
@Profile("in_db_storage")
@Component
public class InDbFilmStorage implements FilmStorage {
    private static final String REQUEST_FILM_BY_ID =    "SELECT f.film_id AS f_id, " +
                                                        "       f.name AS f_name, " +
                                                        "       f.description AS f_description, " +
                                                        "       f.duration AS f_duration, " +
                                                        "       f.likes AS f_likes, " +
                                                        "       r.rating_id AS r_id, " +
                                                        "       r.name AS r_name, " +
                                                        "       f.release_date AS f_date " +
                                                        "FROM (SELECT * FROM films WHERE film_id=?) AS f " +
                                                        "LEFT JOIN ratings AS r ON f.rating_id=r.rating_id";

    private static final String REQUEST_ALL_FILMS = "SELECT f.film_id AS f_id, " +
                                                    "       f.name AS f_name, " +
                                                    "       f.description AS f_description, " +
                                                    "       f.duration AS f_duration, " +
                                                    "       f.likes AS f_likes, " +
                                                    "       r.rating_id AS r_id, " +
                                                    "       r.name AS r_name, " +
                                                    "       f.release_date AS f_date " +
                                                    "FROM films AS f " +
                                                    "LEFT JOIN ratings AS r ON f.rating_id=r.rating_id";

    private static final String REQUEST_TOP_N_FILMS =   "SELECT f.film_id AS f_id, " +
                                                        "       f.name AS f_name, " +
                                                        "       f.description AS f_description, " +
                                                        "       f.duration AS f_duration, " +
                                                        "       f.likes AS f_likes, " +
                                                        "       r.rating_id AS r_id, " +
                                                        "       r.name AS r_name, " +
                                                        "       f.release_date AS f_date " +
                                                        "FROM (SELECT * FROM films " +
                                                        "ORDER BY likes DESC " +
                                                        "LIMIT ?) AS f " +
                                                        "LEFT JOIN ratings AS r ON f.rating_id=r.rating_id";

    private static final String PUT_FILM =  "MERGE INTO films VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String REQUEST_MAX_FILM_ID =   "SELECT max(film_id) AS max_id FROM films";

    private final JdbcTemplate filmsStorage;
    private final FilmGenreStorage filmGenreStorage;

    /**
     * Конструктор класса.
     *
     * @param filmsStorage  объект класса JdbcTemplate;
     */
    @Autowired
    public InDbFilmStorage(JdbcTemplate filmsStorage, FilmGenreStorage filmGenreStorage) {
        this.filmsStorage = filmsStorage;
        this.filmGenreStorage = filmGenreStorage;
    }

    /**
     * Метод возвращает список всех фильмов, находящихся в хранилище в виде Collection<>. Если в хранилище нет ни одного
     * фильма, то метод вернет пустой список.
     *
     * @return список всех фильмов из хранилища; если в ранилище нет ни одного фильма, то метод вернет пустой список.
     */
    @Override
    public Collection<Film> getAllFilms() {
        Stream<Film> filmStream = filmsStorage.queryForStream(REQUEST_ALL_FILMS,
                                  (rs, num) -> createFilmFromResultSet(rs)
                                  );

        Map<Long, Set<Genre>> allFilmsGenres = filmGenreStorage.getGenresForAllFilms();

        return filmStream.peek(film -> film.setGenres(allFilmsGenres.getOrDefault(film.getId(), new HashSet<>())))
               .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Метод возвращает список наиболее популярных фильмов в виде Collection<>. Количество фильмов в списке определяется
     * size. Если в хранилище нет фильмов, то метод вернет пустой список.
     *
     * @param size размер списка фильмов;
     * @return список наиболее популярных фильмов; если в хранилище нет фильмов, то метод вернет пустой список.
     */
    @Override
    public Collection<Film> getTopFilms(int size) {
        return filmsStorage.queryForStream(REQUEST_TOP_N_FILMS, (rs, num) -> createFilmFromResultSet(rs), size)
                .peek(film -> film.setGenres(filmGenreStorage.getGenresForFilmByFilmId(film.getId())))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Метод добавляет item в хранилище.
     *
     * @param item объект для добавления в хранилище;
     */
    @Override
    public void put(Film item) {
        if (item == null) {
            throw new NullArgumentException("Объект равен null");
        }

        filmsStorage.update(PUT_FILM, item.getId(), item.getName(), item.getDescription(), item.getDuration(),
                            item.getRate(), item.getMpa().getId(), item.getReleaseDate());

        filmGenreStorage.putGenresForFilm(item.getId(), item.getGenres());
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
    public Optional<Film> get(long key) {
        Optional<Film> wrappedFilm = Optional.ofNullable(filmsStorage.query(REQUEST_FILM_BY_ID, (rs) -> {
            if (rs.next()) {
                return createFilmFromResultSet(rs);
            }
            return null;
        }, key));

        wrappedFilm.ifPresent(film -> film.setGenres(filmGenreStorage.getGenresForFilmByFilmId(key)));

        return wrappedFilm;
    }

    /**
     * Метод возвращает наибольшее значение идентификатора фильма из БД.
     *
     * @return  максимальное значение идентификатора фильма.
     */
    public Optional<Long> getMaxFilmId() {
        return Optional.ofNullable(filmsStorage.query(REQUEST_MAX_FILM_ID, rs -> {
            if (rs.next()) {
                return rs.getLong("max_id");
            }
            return null;
        }));
    }

    private Film createFilmFromResultSet(ResultSet rs) throws SQLException {
        var film = new Film(rs.getString("f_name"), rs.getString("f_description"), rs.getDate("f_date").toLocalDate(),
                   rs.getInt("f_duration"));
        film.setId(rs.getLong("f_id"));
        film.setRate(rs.getInt("f_likes"));
        film.setMpa(new Mpa(rs.getInt("r_id"), rs.getString("r_name")));
        return film;
    }
}
