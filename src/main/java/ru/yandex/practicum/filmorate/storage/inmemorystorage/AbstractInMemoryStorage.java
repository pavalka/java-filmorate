package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import ru.yandex.practicum.filmorate.storage.AddToStorageException;
import ru.yandex.practicum.filmorate.storage.DeleteFromStorageException;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.UpdateStorageException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractInMemoryStorage<T, K> implements Storage<T, K> {
    protected final Map<K, T> storage = new HashMap<>();

    /**
     * Метод добавляет item в хранилище. При возникновении ошибки в процеесе сохранения метод генерирует исключение
     * {@link AddToStorageException}.
     *
     * @param key  ключ, под которым будет сохранено item;
     * @param item объект для добавления в хранилище;
     */
    @Override
    public void add(K key, T item) throws AddToStorageException {
        if (key == null || item == null) {
            throw new AddToStorageException("Объект равен null");
        }

        if (storage.containsKey(key)) {
            throw new AddToStorageException("Объект с key = " + key + " уже есть в хранилище.");
        }

        storage.put(key, item);
    }

    /**
     * Метод обновляет в хранилище объект item. Если item не найден в хранилище или при возникновении в процессе
     * обновления другой ошибки, метод генерирует исключение {@link UpdateStorageException}.
     *
     * @param key  ключ, значение под которым нужно обновить;
     * @param item объект для обновления;
     * @throws UpdateStorageException генерируется если объект item не найден в хранилище или, если в процессе
     *                                обновления произошла другая ошибка.
     */
    @Override
    public void update(K key, T item) throws UpdateStorageException {
        if (key == null || item == null) {
            throw new UpdateStorageException("Объект равен null");
        }

        if (!storage.containsKey(key)) {
            throw new UpdateStorageException("Объект с key = " + key + " не найден в хранилище.");
        }

        storage.put(key, item);
    }

    /**
     * Метод удаляет объект с ключом key из хранилища. Если объект с ключом key не найден в хранили или
     * если в процессе удаления происходит другая ошибка, то метод генерирует исключение
     * {@link DeleteFromStorageException}.
     *
     * @param key ключ объекта, который нужно удалить;
     * @throws DeleteFromStorageException генерируется если объект с ключом key не найден в хранили или
     *                                    если в процессе удаления происходит другая ошибка
     */
    @Override
    public void delete(K key) throws DeleteFromStorageException {
        if (key == null) {
            throw new DeleteFromStorageException("Объект равен null");
        }

        if (storage.remove(key) == null) {
            throw new DeleteFromStorageException("Объект с key = " + key + " не найден в хранилище.");
        }
    }

    /**
     * Метод возвращает из хранилища объект с ключом key, обернутый в класс {@link Optional}. Если объект с
     * ключом key не найден в хранилище, то в {@link Optional} будет помещен null.
     *
     * @param key ключ объекта, который мы хотим получить;
     * @return объект с ключом key, обернутый в класс {@link Optional}.
     * Если объект с ключом key не найден в хранилище, то в {@link Optional} будет помещен null
     */
    @Override
    public Optional<T> get(K key) {
        if (key == null) {
            return Optional.empty();
        }

        return Optional.of(storage.get(key));
    }
}
