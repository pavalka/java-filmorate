package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AddToStorageException;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

/**
 * Класс реализует хранение объектов класса User в ОЗУ.
 */
@Component
public class InMemoryUserStorage extends AbstractInMemoryStorage<User, Long> implements UserStorage {

    /**
     * Метод добавляет item в хранилище. При возникновении ошибки в процеесе сохранения метод генерирует исключение
     *{@link AddToStorageException}.
     *
     * @param key  ключ, под которым будет сохранено item;
     * @param item объект для добавления в хранилище;
     */
    @Override
    public void add(Long key, User item) throws AddToStorageException {
        if (key == null || item == null) {
            throw new AddToStorageException("Объект равен null");
        }

        // Проверяем есть ли уже в хранилище такой же пользователь (пользователь с таким же email)

        if (storage.containsValue(item)) {
            throw new AddToStorageException(String.format("Пользователь с email = %s уже существует.", item.getEmail()));
        }

        storage.put(key, item);
    }

    /**
     * Метод возвращает список всех пользователей, находящихся в хранилище в виде Collection<>. Если в хранилище нет ни
     * одного пользователя, то метод вернет пустой список.
     *
     * @return список всех пользователей из хранилища; если в хранилище нет ни одного пользователя, то метод вернет
     * пустой список.
     */
    @Override
    public Collection<User> getAllUsers() {
        return storage.values();
    }
}
