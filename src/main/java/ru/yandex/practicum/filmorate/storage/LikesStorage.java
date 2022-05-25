package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Likes;

import java.util.Collection;

/**
 * Определяет функционал хранилища лайков.
 */
public interface LikesStorage extends Storage<Likes, Long> {
    /**
     * Метод возвращает список наиболее популярных фильмов в виде Collection<>. Количество фильмов в списке определяется
     * size. Если в хранилище нет фильмов, то метод вернет пустой список.
     *
     * @param size размер списка фильмов;
     * @return  список наиболее популярных фильмов; если в хранилище нет фильмов, то метод вернет пустой список.
     */
    Collection<Likes> getTopFilms(int size);
}
