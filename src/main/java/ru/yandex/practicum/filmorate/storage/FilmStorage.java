package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

/**
 * Определяет функционал хранилища фильмов.
 */
public interface FilmStorage extends Storage <Film, Long> {
    /**
     * Метод возвращает список наиболее популярных фильмов. Количество фильмов в списке определяется size. Если в
     * хранилище нет фильмов, то метод вернет пустой список.
     *
     * @param size размер списка фильмов;
     * @return если в хранилище нет фильмов, то метод вернет пустой список.
     */
    Collection<Film> getTopFilms(int size);
}
