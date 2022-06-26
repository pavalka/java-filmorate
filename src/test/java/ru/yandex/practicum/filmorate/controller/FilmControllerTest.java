package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmNotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.inmemorystorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.inmemorystorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmControllerTest {
    private FilmService filmService;

    @BeforeEach
    public void runBeforeEachTest() {
        filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage(), null, null,
                new IdGenerator());
    }

    @Test
    void getAllFilmsShouldReturnEmptyCollectionWhenNoFilmsAreAdded() {
        assertTrue(filmService.getAllFilms().isEmpty());
    }

    @Test
    void getAllFilmsShouldReturnFilmsCollectionWhenOneFilmIsAdded() {
        try {
            Film film = new Film("Test film", "Film description", LocalDate.of(2001, 2, 14), 90);
            film.setMpa(new Mpa(1, "G"));
            filmService.addFilm(film);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }

        assertEquals(1, filmService.getAllFilms().size());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddNull() {
        assertThrows(ValidationException.class, () -> filmService.addFilm(null));
        assertTrue(filmService.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddFilmWithEmptyName() {
        Film film = new Film("", "Description", LocalDate.of(2001, 2,14), 90);

        assertThrows(ValidationException.class, () -> filmService.addFilm(film));
        assertTrue(filmService.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddFilmWithEmptyDescription() {
        Film film = new Film("Test film", "", LocalDate.of(2001, 2, 14), 90);
        assertThrows(ValidationException.class, () -> filmService.addFilm(film));
        assertTrue(filmService.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddFilmWithDescriptionLengthIs201() {
        Film film = new Film("Test film", "Description".repeat(18) + "End", LocalDate.of(2001, 2, 14), 90);
        assertThrows(ValidationException.class, () -> filmService.addFilm(film));
        assertTrue(filmService.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddFilmWithWrongDate() {
        Film film = new Film("Test film", "Description", LocalDate.of(1895, 12, 17), 90);
        assertThrows(ValidationException.class, () -> filmService.addFilm(film));
        assertTrue(filmService.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddFilmWithDateAfterNow() {
        Film film = new Film("Test film", "Description", LocalDate.now().plusDays(1), 90);
        assertThrows(ValidationException.class, () -> filmService.addFilm(film));
        assertTrue(filmService.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddFilmWithNegativeDuration() {
        Film film = new Film("Test Film", "Description", LocalDate.of(2001, 2, 14), -1);
        assertThrows(ValidationException.class, () -> filmService.addFilm(film));
        assertTrue(filmService.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldAddFilm() {
        Film film = new Film("Test film", "Description", LocalDate.of(2001, 2, 14), 90);
        film.setMpa(new Mpa(1, "G"));
        assertDoesNotThrow(() -> filmService.addFilm(film));
        assertEquals(1, filmService.getAllFilms().size());
    }

    @Test
    void updateFilmShouldThrowExceptionWhenUpdateNull() {
        assertThrows(ValidationException.class, () -> filmService.updateFilm(null));
    }

    @Test
    void updateFilmShouldThrowExceptionWhenFilmIdIsWrong() {
        Film film = new Film("Test film", "Description", LocalDate.of(2001, 2, 14), 90);
        film.setMpa(new Mpa(1, "G"));
        assertDoesNotThrow(() -> filmService.addFilm(film));

        Film newFilm = new Film("Test film", "Description - 1", LocalDate.of(2001, 2, 14), 90);
        newFilm.setMpa(new Mpa(1, "G"));
        newFilm.setId(100000);
        assertThrows(FilmNotFoundException.class, () -> filmService.updateFilm(newFilm));
    }

    @Test
    void updateFilmShouldUpdateFilm() {
        Film film = new Film("Test film", "Description", LocalDate.of(2001, 2, 14), 90);
        film.setMpa(new Mpa(1, "G"));
        assertDoesNotThrow(() -> filmService.addFilm(film));
        Film newFilm = new Film("Test film", "Description-1", LocalDate.of(2001, 2, 14), 90);
        newFilm.setMpa(new Mpa(1, "G"));
        newFilm.setId(film.getId());
        assertDoesNotThrow(() -> filmService.updateFilm(newFilm));
        for (Film currentFilm : filmService.getAllFilms()) {
            assertEquals(newFilm.getId(), currentFilm.getId());
            assertEquals(newFilm.getName(), currentFilm.getName());
            assertEquals(newFilm.getDescription(), currentFilm.getDescription());
            assertEquals(newFilm.getReleaseDate(), currentFilm.getReleaseDate());
            assertEquals(newFilm.getDuration(), currentFilm.getDuration());
        }
    }
}