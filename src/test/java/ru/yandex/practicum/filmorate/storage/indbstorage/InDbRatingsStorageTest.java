package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.RatingsStorage;

@SpringBootTest
@AutoConfigureTestDatabase
class InDbRatingsStorageTest {
    private final RatingsStorage ratingsStorage;

    @Autowired
    public InDbRatingsStorageTest(RatingsStorage ratingsStorage) {
        this.ratingsStorage = ratingsStorage;
    }

    @Test
    void getAllRatingsShouldReturnRatings() {
        var ratings = Assertions.assertDoesNotThrow(ratingsStorage::getAllRatings);

        Assertions.assertEquals(5, ratings.size());
        Assertions.assertTrue(ratings.contains(new Mpa(1, "G")));
        Assertions.assertTrue(ratings.contains(new Mpa(4, "R")));
    }

    @Test
    void getRatingByIdShouldReturnRating() {
        var rating = Assertions.assertDoesNotThrow(() -> ratingsStorage.getRatingById(2));

        Assertions.assertTrue(rating.isPresent());
        Assertions.assertEquals(2, rating.get().getId());
        Assertions.assertEquals("PG", rating.get().getName());
    }

    @Test
    void getRatingByIdShouldReturnEmptyOptional() {
        var rating = Assertions.assertDoesNotThrow(() -> ratingsStorage.getRatingById(10));

        Assertions.assertTrue(rating.isEmpty());
    }
}