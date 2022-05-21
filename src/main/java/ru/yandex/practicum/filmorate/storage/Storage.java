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
     * @param item объект для добавления в хранилище;
     */
    void add(T item) throws AddToStorageException;

    /**
     * Метод обновляет в хранилище объект item. Если item не найден в хранилище или при возникновении в процессе
     * обновления другой ошибки, метод генерирует исключение {@link UpdateStorageException}.
     *
     * @param item объект для обновления;
     * @throws UpdateStorageException   генерируется если объект item не найден в хранилище или, если в процессе
     *         обновления произошла другая ошибка.
     */
    void update(T item) throws UpdateStorageException;

    /**
     * Метод удаляет объект с идентификатором id из хранилища. Если объект с идентификатором id не найден в хранили или
     * если в процессе удаления происходит другая ошибка, то метод генерирует исключение
     * {@link DeleteFromStorageException}.
     *
     * @param id    идентификатор объекта, который нужно удалить;
     * @throws DeleteFromStorageException   генерируется если объект с идентификатором id не найден в хранили или
     *         если в процессе удаления происходит другая ошибка
     */
    void delete(K id) throws DeleteFromStorageException;

    /**
     * Метод возвращает из хранилища объект с идентификатором id, обернутый в класс {@link Optional}. Если объект с
     * идентификатором id не найден в хранилище, то в {@link Optional} будет помещен null.
     *
     * @param id идентификатор объекта, который мы хотим получить;
     * @return объект с идентификатором id, обернутый в класс {@link Optional}.
     *         Если объект с идентификатором id не найден в хранилище, то в {@link Optional} будет помещен null
     */
    Optional<T> get(K id);
}