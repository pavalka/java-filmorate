package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Реализует интерфейс {@link GenresStorage} и использует ОЗУ для хранения списка жанров.
 */
@Component
@Profile("in_memory_storage")
public class InMemoryGenresStorage implements GenresStorage {
    private final Map<Integer, Genre> genresStorage;

    public InMemoryGenresStorage() {
        genresStorage = new HashMap<>();
        genresStorage.put(1, new Genre(1, "Комедия"));
        genresStorage.put(2, new Genre(2, "Драма"));
        genresStorage.put(3, new Genre(3, "Мультфильм"));
        genresStorage.put(4, new Genre(4, "Триллер"));
        genresStorage.put(5, new Genre(5, "Документальный"));
        genresStorage.put(6, new Genre(6, "Боевик"));
    }

    /**
     * Метод возвращает список всех жанроов в хранилище. Если в хранилище нет ни одного жанра, то метод вернет пустой
     * список.
     *
     * @return список всех жанроов в хранилище; пустой список, если в хранилище нет ни одного жанра;
     */
    @Override
    public Collection<Genre> getAllGenres() {
        return genresStorage.values();
    }

    /**
     * Метод возвращает жанр с идентификатором genreId. Если жанр с таким идентификатором не найден метод вренет пустой
     * Optional.
     *
     * @param genreId идентификатор жанра;
     * @return жанр с заданным идентификатором; пустой объект Optional, если жанр с ижентификатором genreId не найден.
     */
    @Override
    public Optional<Genre> getGenreById(int genreId) {
        return Optional.ofNullable(genresStorage.get(genreId));
    }
}
