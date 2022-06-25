package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullArgumentException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

/**
 * Класс реализует хранение объектов класса User в ОЗУ.
 */
@Profile("in_memory_storage")
@Component
public class InMemoryUserStorage extends AbstractInMemoryStorage<User> implements UserStorage {

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

    /**
     * Метод проверяет присутствует ли в хранилище пользователь user. Если этот пользователь есть в хранилище
     * пользователей, то метод вернет true. В противном случает метод вернет false.
     *
     * @param user пользователь, которого нужно проверить;
     * @return если этот пользователь есть в хранилище пользователей, то метод вернет true; в противном случает метод
     * вернет false.
     */
    @Override
    public boolean isUserPresent(User user) {
        if (user == null) {
            throw new NullArgumentException("Объект равен null");
        }

        return storage.containsValue(user);
    }
}
