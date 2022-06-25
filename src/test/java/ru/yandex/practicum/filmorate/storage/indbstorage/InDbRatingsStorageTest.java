package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.RatingsStorage;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("in_db_storage")
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
}