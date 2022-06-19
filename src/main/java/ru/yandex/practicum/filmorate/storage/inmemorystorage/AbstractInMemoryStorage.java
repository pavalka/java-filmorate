package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import ru.yandex.practicum.filmorate.exception.NullArgumentException;
import ru.yandex.practicum.filmorate.model.Id;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractInMemoryStorage<T extends Id> implements Storage<T> {
    protected final Map<Long, T> storage = new HashMap<>();

    /**
     * Метод добавляет item в хранилище. Если key = null или item = null, то метод сгенерирует исключение
     * {@link NullArgumentException}.
     *
     * @param item объект для добавления в хранилище;
     */
    @Override
    public void put(T item) {
        if (item == null) {
            throw new NullArgumentException("Объект равен null");
        }

        storage.put(item.getId(), item);
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
    public Optional<T> get(long key) {
        return Optional.ofNullable(storage.get(key));
    }
}
