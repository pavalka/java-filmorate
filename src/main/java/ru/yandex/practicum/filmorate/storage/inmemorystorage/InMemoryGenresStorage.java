package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Реализует интерфейс {@link GenresStorage} и использует ОЗУ для хранения списка жанров.
 */
@Component
@Profile("in_memory_storage")
public class InMemoryGenresStorage implements GenresStorage {
    private final Set<Genre> genresStorage;

    public InMemoryGenresStorage() {
        genresStorage = new HashSet<>();
        genresStorage.add(new Genre(1, "Комедия"));
        genresStorage.add(new Genre(2, "Драма"));
        genresStorage.add(new Genre(3, "Мультфильм"));
        genresStorage.add(new Genre(4, "Триллер"));
        genresStorage.add(new Genre(5, "Документальный"));
        genresStorage.add(new Genre(6, "Боевик"));
    }

    /**
     * Метод возвращает список всех жанроов в хранилище. Если в хранилище нет ни одного жанра, то метод вернет пустой
     * список.
     *
     * @return список всех жанроов в хранилище; пустой список, если в хранилище нет ни одного жанра;
     */
    @Override
    public Collection<Genre> getAllGenres() {
        return genresStorage;
    }
}
