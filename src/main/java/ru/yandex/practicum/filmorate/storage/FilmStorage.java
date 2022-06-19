package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

/**
 * Определяет функционал хранилища фильмов.
 */
public interface FilmStorage extends Storage <Film> {

    /**
     * Метод возвращает список всех фильмов, находящихся в хранилище в виде Collection<>. Если в хранилище нет ни одного
     * фильма, то метод вернет пустой список.
     *
     * @return  список всех фильмов из хранилища; если в ранилище нет ни одного фильма, то метод вернет пустой список.
     */
    Collection<Film> getAllFilms();

    /**
     * Метод возвращает список наиболее популярных фильмов в виде Collection<>. Количество фильмов в списке определяется
     * size. Если в хранилище нет фильмов, то метод вернет пустой список.
     *
     * @param size размер списка фильмов;
     * @return  список наиболее популярных фильмов; если в хранилище нет фильмов, то метод вернет пустой список.
     */
    Collection<Film> getTopFilms(int size);
}
