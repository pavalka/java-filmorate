package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresStorage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("in_db_storage")
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
}