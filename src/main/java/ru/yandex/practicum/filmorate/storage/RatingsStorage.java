package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

/**
 * Реализует функции хранилища рейтингов.
 */
public interface RatingsStorage {
    /**
     * Метод возвращает список всех ретингов в хранилище. Если в хранилище нет ни одного рейтинга, то метод вернет
     * пустой список.
     *
     * @return  список всех ретингов в хранилище; пустой список, если в хранилище нет ни одного рейтинга.
     */
    Collection<Mpa> getAllRatings();

    /**
     * Метод возвращает райтинг фильма с идентификатором ratingId. Если рейтинг с таким идентификатором не найден, то
     * метод вернет пустой Optional.
     *
     * @param ratingId  идентификатор рейтинга фильма;
     * @return  райтинг фильма; пустой объект {@link Optional}, если рейтинг с таким идентификатором не найден.
     */
    Optional<Mpa> getRatingById(int ratingId);
}
