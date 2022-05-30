package ru.yandex.practicum.filmorate.storage;

import java.util.Optional;

/**
 * Интерфейс, описывающий функционал хранилища объектов клааса T.
 * @param <T> тип хранимых объектов.
 */
public interface Storage <T, K>{
    /**
     * Метод добавляет item в хранилище.
     *
     *  @param key  ключ, под которым будет сохранено item;
     *  @param item объект для добавления в хранилище;
     */
    void put(K key, T item);

    /**
     * Метод возвращает из хранилища объект с ключом key, обернутый в класс {@link Optional}. Если объект с
     * ключом key не найден в хранилище, то в {@link Optional} будет помещен null.
     *
     * @param key ключ объекта, который мы хотим получить;
     * @return объект с ключом key, обернутый в класс {@link Optional}.
     *         Если объект с ключом key не найден в хранилище, то в {@link Optional} будет помещен null
     */
    Optional<T> get(K key);
}