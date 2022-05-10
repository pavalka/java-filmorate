package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void runBeforeEachTest() {
        filmController = new FilmController();
    }

    @Test
    void getAllFilmsShouldReturnEmptyCollectionWhenNoFilmsAreAdded() {
        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void getAllFilmsShouldReturnFilmsCollectionWhenOneFilmIsAdded() {
        try {
            filmController.addFilm(new Film("Test film", "Film description", LocalDate.of(2001, 2, 14), 90));
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }

        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddNull() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(null));
        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddFilmWithEmptyName() {
        Film film = new Film("", "Description", LocalDate.of(2001, 2,14), 90);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddFilmWithEmptyDescription() {
        Film film = new Film("Test film", "", LocalDate.of(2001, 2, 14), 90);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddFilmWithDescriptionLengthIs201() {
        Film film = new Film("Test film", "Description".repeat(18) + "End", LocalDate.of(2001, 2, 14), 90);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddFilmWithWrongDate() {
        Film film = new Film("Test film", "Description", LocalDate.of(1895, 12, 17), 90);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddFilmWithDateAfterNow() {
        Film film = new Film("Test film", "Description", LocalDate.now().plusDays(1), 90);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldThrowExceptionWhenAddFilmWithNegativeDuration() {
        Film film = new Film("Test Film", "Description", LocalDate.of(2001, 2, 14), -1);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void addFilmShouldAddFilm() {
        Film film = new Film("Test film", "Description", LocalDate.of(2001, 2, 14), 90);
        assertDoesNotThrow(() -> filmController.addFilm(film));
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    void updateFilmShouldThrowExceptionWhenUpdateNull() {
        assertThrows(ValidationException.class, () -> filmController.updateFilm(null));
    }

    @Test
    void updateFilmShouldThrowExceptionWhenFilmIdIsWrong() {
        Film film = new Film("Test film", "Description", LocalDate.of(2001, 2, 14), 90);
        assertDoesNotThrow(() -> filmController.addFilm(film));

        Film newFilm = new Film("Test film", "Description - 1", LocalDate.of(2001, 2, 14), 90);
        newFilm.setId(100000);
        assertThrows(UpdateException.class, () -> filmController.updateFilm(newFilm));
    }

    @Test
    void updateFilmShouldUpdateFilm() {
        Film film = new Film("Test film", "Description", LocalDate.of(2001, 2, 14), 90);
        assertDoesNotThrow(() -> filmController.addFilm(film));
        Film newFilm = new Film("Test film", "Description-1", LocalDate.of(2001, 2, 14), 90);
        newFilm.setId(film.getId());
        assertDoesNotThrow(() -> filmController.updateFilm(newFilm));
        for (Film currentFilm : filmController.getAllFilms()) {
            assertEquals(newFilm.getId(), currentFilm.getId());
            assertEquals(newFilm.getName(), currentFilm.getName());
            assertEquals(newFilm.getDescription(), currentFilm.getDescription());
            assertEquals(newFilm.getReleaseDate(), currentFilm.getReleaseDate());
            assertEquals(newFilm.getDuration(), currentFilm.getDuration());
        }
    }
}