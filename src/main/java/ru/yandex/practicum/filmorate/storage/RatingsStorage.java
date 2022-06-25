package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

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
}
