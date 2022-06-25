package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

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
}
