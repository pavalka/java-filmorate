package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("in_db_storage")
class InDbFilmGenreStorageTest {
    private final FilmGenreStorage filmGenreStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InDbFilmGenreStorageTest(FilmGenreStorage filmGenreStorage, JdbcTemplate jdbcTemplate) {
        this.filmGenreStorage = filmGenreStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    private void addGenresForFilmToDb() {
        jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (1, 1), (1, 3)");
    }
    private void addFilmToDb() {
        jdbcTemplate.update("INSERT INTO films (film_id, name, description, duration, likes, rating_id, release_date) " +
                            "VALUES (1, 'Film 1', 'Description of Film 1', 90, 5, 2, DATE '1990-03-20')");
    }

    private void addFilmAndGenresToDb() {
        addFilmToDb();
        addGenresForFilmToDb();
    }

    @AfterEach
    public void deleteAllFilmsFromDb() {
        jdbcTemplate.update("DELETE FROM films");
    }

    @Test
    void getGenresForFilmByFilmIdReturnEmptyGenresSet() {
        var genres = Assertions.assertDoesNotThrow(() -> filmGenreStorage.getGenresForFilmByFilmId(1));
        Assertions.assertTrue(genres.isEmpty());
    }

    @Test
    void getGenresForFilmByFilmIdReturnGenres() {
        addFilmAndGenresToDb();
        var genres = Assertions.assertDoesNotThrow(() -> filmGenreStorage.getGenresForFilmByFilmId(1));
        Assertions.assertEquals(2, genres.size());
    }

    @Test
    void getGenresForAllFilmsReturnEmptyMap() {
        var genres = Assertions.assertDoesNotThrow(filmGenreStorage::getGenresForAllFilms);
        Assertions.assertTrue(genres.isEmpty());
    }

    @Test
    void getGenresForAllFilmsReturnGenres() {
        addFilmAndGenresToDb();
        var genres = Assertions.assertDoesNotThrow(filmGenreStorage::getGenresForAllFilms);
        Assertions.assertEquals(1, genres.size());
    }

    @Test
    void putGenresForFilmAddGenres() {
        addFilmToDb();
        var genres = Set.of(new Genre(1, "Комедия"), new Genre(3, "Мультфильм"));
        Assertions.assertDoesNotThrow(() -> filmGenreStorage.putGenresForFilm(1, genres));
        Assertions.assertEquals(2, filmGenreStorage.getGenresForFilmByFilmId(1).size());
    }

    @Test
    void putGenresForFilmThrowsException() {
        var genres = Set.of(new Genre(1, "Комедия"), new Genre(3, "Мультфильм"));
        Assertions.assertThrows(DataAccessException.class, () -> filmGenreStorage.putGenresForFilm(1, genres));
    }
}