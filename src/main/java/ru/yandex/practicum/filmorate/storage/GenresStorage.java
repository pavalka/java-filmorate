package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

/**
 * Определяет функции хранилища жанров.
 */
public interface GenresStorage {
    /**
     * Метод возвращает список всех жанроов в хранилище. Если в хранилище нет ни одного жанра, то метод вернет пустой
     * список.
     *
     * @return  список всех жанроов в хранилище; пустой список, если в хранилище нет ни одного жанра;
     */
    Collection<Genre> getAllGenres();

    /**
     * Метод возвращает жанр с идентификатором genreId. Если жанр с таким идентификатором не найден метод вренет пустой
     * Optional.
     *
     * @param genreId   идентификатор жанра;
     * @return  жанр с заданным идентификатором; пустой объект Optional, если жанр с ижентификатором genreId не найден.
     */
    Optional<Genre> getGenreById(int genreId);
}
