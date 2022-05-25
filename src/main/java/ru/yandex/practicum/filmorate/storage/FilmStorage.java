package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

/**
 * Определяет функционал хранилища фильмов.
 */
public interface FilmStorage extends Storage <Film, Long> {

    /**
     * Метод возвращает список всех фильмов, находящихся в хранилище в виде Collection<>. Если в хранилище нет ни одного
     * фильма, то метод вернет пустой список.
     *
     * @return  список всех фильмов из хранилища; если в ранилище нет ни одного фильма, то метод вернет пустой список.
     */
    Collection<Film> getAllFilms();
}
