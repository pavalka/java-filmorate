package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("in_db_storage")
class InDbFilmStorageTest {
    private final FilmStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InDbFilmStorageTest(FilmStorage filmStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    private void addFilmToDb() {
        jdbcTemplate.update("INSERT INTO films (film_id, name, description, duration, likes, rating_id, release_date) " +
                "VALUES (1, 'Film 1', 'Description of Film 1', 90, 5, 2, DATE '1990-03-20')");
        jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (1, 1), (1, 3)");
    }

    private void addSecondFilmToDb() {
        jdbcTemplate.update("INSERT INTO films (film_id, name, description, duration, likes, rating_id, release_date) " +
                            "VALUES (2, 'Film 2', 'Description of Film 2', 100, 7, 1, DATE '1989-04-21')");
        jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (2, 1)");
    }

    @AfterEach
    public void deleteAllFilmsFromDb() {
        jdbcTemplate.update("DELETE FROM films");
    }

    @Test
    void getAllFilmsReturnEmptyFilmCollection() {
        var films = Assertions.assertDoesNotThrow(filmStorage::getAllFilms);
        Assertions.assertTrue(films.isEmpty());
    }

    @Test
    void getAllFilmsReturnFilmsCollection() {
        addFilmToDb();
        var films = Assertions.assertDoesNotThrow(filmStorage::getAllFilms);
        Assertions.assertEquals(1, films.size());

        for (Film film : films) {
            Assertions.assertEquals(1, film.getId());
            Assertions.assertEquals("Film 1", film.getName());
            Assertions.assertEquals(2, film.getGenres().size());
        }
    }

    @Test
    void getTopFilmsReturnEmptyFilmsCollection() {
        var films = Assertions.assertDoesNotThrow(() -> filmStorage.getTopFilms(3));
        Assertions.assertTrue(films.isEmpty());
    }

    @Test
    void getTopFilmsReturnFilmsCollection() {
        addFilmToDb();
        addSecondFilmToDb();

        var films = Assertions.assertDoesNotThrow(() -> filmStorage.getTopFilms(3));
        int[] filmsRate = {7, 5};
        long[] filmsId = {2, 1};
        int i = 0;

        Assertions.assertEquals(2, films.size());
        for (Film film : films) {
            Assertions.assertEquals(filmsId[i], film.getId());
            Assertions.assertEquals(filmsRate[i], film.getRate());
            i++;
        }
    }

    @Test
    void putAddFilmToDb() {
        Set<Genre> genres = Set.of(new Genre(1, "Комедия"));
        var film = new Film(1, "Film 1", "Description of Film 1", LocalDate.of(1990, 3, 20), 90, 5,
                       new Mpa(2, "PG"), genres);

        Assertions.assertDoesNotThrow(() -> filmStorage.put(film));

        Optional<Film> wrappedFilm = filmStorage.get(1);

        Assertions.assertTrue(wrappedFilm.isPresent());
        Assertions.assertEquals(1, wrappedFilm.get().getId());
        Assertions.assertEquals(1, wrappedFilm.get().getGenres().size());
    }

    @Test
    void putUpdateFilmGenresInDb() {
        addFilmToDb();

        Film film = filmStorage.get(1).get();

        Assertions.assertEquals(1, film.getId());
        Assertions.assertEquals(2, film.getGenres().size());
        film.setGenres(Set.of(new Genre(1, "комедия")));
        Assertions.assertDoesNotThrow(() -> filmStorage.put(film));
        Film newFilm = filmStorage.get(1).get();
        Assertions.assertEquals(1, newFilm.getId());
        Assertions.assertEquals(1, newFilm.getGenres().size());
    }

    @Test
    void getReturnEmptyOptional() {
        Optional<Film> wrappedFilm = Assertions.assertDoesNotThrow(() -> filmStorage.get(3));
        Assertions.assertTrue(wrappedFilm.isEmpty());
    }

    @Test
    void getReturnFilm() {
        addFilmToDb();

        Optional<Film> wrappedFilm = Assertions.assertDoesNotThrow(() -> filmStorage.get(1));
        Assertions.assertTrue(wrappedFilm.isPresent());
        Assertions.assertEquals(1, wrappedFilm.get().getId());
        Assertions.assertEquals("Film 1", wrappedFilm.get().getName());
        Assertions.assertEquals(LocalDate.of(1990, 3, 20), wrappedFilm.get().getReleaseDate());
        Assertions.assertEquals(2, wrappedFilm.get().getGenres().size());
    }
}