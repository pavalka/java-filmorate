package ru.yandex.practicum.filmorate.storage;

import java.util.Optional;

/**
 * Интерфейс, описывающий функционал хранилища объектов клааса T.
 * @param <T> тип хранимых объектов.
 */
public interface Storage <T, K>{
    /**
     * Метод добавляет item в хранилище. При возникновении ошибки в процеесе сохранения метод генерирует исключение
     *{@link AddToStorageException}.
     *
     * @param key  ключ, под которым будет сохранено item;
     * @param item объект для добавления в хранилище;
     */
    void add(K key, T item) throws AddToStorageException;

    /**
     * Метод обновляет в хранилище объект item. Если item не найден в хранилище или при возникновении в процессе
     * обновления другой ошибки, метод генерирует исключение {@link UpdateStorageException}.
     *
     *
     * @param key  ключ, значение под которым нужно обновить;
     * @param item объект для обновления;
     * @throws UpdateStorageException   генерируется если объект item не найден в хранилище или, если в процессе
     *         обновления произошла другая ошибка.
     */
    void update(K key, T item) throws UpdateStorageException;

    /**
     * Метод удаляет объект с ключом key из хранилища. Если объект с ключом key не найден в хранили или
     * если в процессе удаления происходит другая ошибка, то метод генерирует исключение
     * {@link DeleteFromStorageException}.
     *
     * @param key    ключ объекта, который нужно удалить;
     * @throws DeleteFromStorageException   генерируется если объект с ключом key не найден в хранили или
     *         если в процессе удаления происходит другая ошибка
     */
    void delete(K key) throws DeleteFromStorageException;

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