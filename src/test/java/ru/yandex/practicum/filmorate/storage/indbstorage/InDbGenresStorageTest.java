package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresStorage;

@SpringBootTest
@AutoConfigureTestDatabase
class InDbGenresStorageTest {
    private final GenresStorage genresStorage;

    @Autowired
    public InDbGenresStorageTest(GenresStorage genresStorage){
        this.genresStorage = genresStorage;
    }

    @Test
    void getAllGenresShouldReturnGenres() {
        var genres = Assertions.assertDoesNotThrow(genresStorage::getAllGenres);

        Assertions.assertEquals(6, genres.size());
        Assertions.assertTrue(genres.contains(new Genre(1, "Комедия")));
        Assertions.assertTrue(genres.contains(new Genre(4, "Триллер")));
    }

    @Test
    void getGenreByIdShouldReturnGenre() {
        var genre = Assertions.assertDoesNotThrow(() -> genresStorage.getGenreById(3));

        Assertions.assertTrue(genre.isPresent());
        Assertions.assertEquals(3, genre.get().getId());
        Assertions.assertEquals("Мультфильм", genre.get().getName());
    }

    @Test
    void getGenreByIdShouldReturnEmptyOptional() {
        var genre = Assertions.assertDoesNotThrow(() -> genresStorage.getGenreById(10));

        Assertions.assertTrue(genre.isEmpty());
    }
}