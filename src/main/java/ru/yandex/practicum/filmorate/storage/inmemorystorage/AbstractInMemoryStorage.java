package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import ru.yandex.practicum.filmorate.exception.NullArgumentException;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractInMemoryStorage<T, K> implements Storage<T, K> {
    protected final Map<K, T> storage = new HashMap<>();

    /**
     * Метод добавляет item в хранилище. Если key = null или item = null, то метод сгенерирует исключение
     * {@link NullArgumentException}.
     *
     *  @param key  ключ, под которым будет сохранено item;
     *  @param item объект для добавления в хранилище;
     */
    @Override
    public void put(K key, T item) {
        if (key == null || item == null) {
            throw new NullArgumentException("Объект равен null");
        }

        storage.put(key, item);
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

        return Optional.ofNullable(storage.get(key));
    }

    /**
     * Метод проверяет есть ли в хранилище объект с ключом key. Если объект присутствует в хранилище, то метод вернет
     * true. В противном случает возвращаемое значение - false.
     *
     * @param key ключ, который нужно проверить;
     * @return если объект присутствует в хранилище, то метод вернет true; в противном случает возвращаемое значение
     * - false.
     */
    @Override
    public boolean isKeyPresent(K key) {
        if (key == null) {
            throw new NullArgumentException("Объект равен null.");
        }

        return storage.containsKey(key);
    }
}
