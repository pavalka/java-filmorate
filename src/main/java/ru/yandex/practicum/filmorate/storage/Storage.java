package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Id;

import java.util.Optional;

/**
 * Интерфейс, описывающий функционал хранилища объектов клааса T.
 * @param <T> тип хранимых объектов.
 */
public interface Storage <T extends Id>{
    /**
     * Метод добавляет item в хранилище.
     *
     * @param item объект для добавления в хранилище;
     */
    void put(T item);

    /**
     * Метод возвращает из хранилища объект с ключом key, обернутый в класс {@link Optional}. Если объект с
     * ключом key не найден в хранилище, то в {@link Optional} будет помещен null.
     *
     * @param key ключ объекта, который мы хотим получить;
     * @return объект с ключом key, обернутый в класс {@link Optional}.
     *         Если объект с ключом key не найден в хранилище, то в {@link Optional} будет помещен null
     */
    Optional<T> get(long key);
}