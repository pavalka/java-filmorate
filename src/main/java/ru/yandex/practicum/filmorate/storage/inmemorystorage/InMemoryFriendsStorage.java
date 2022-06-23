package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

/**
 * Класс реализует функционал хранилища списка друзей пользователя.
 */
@Component("inMemoryFriendsStorage")
public class InMemoryFriendsStorage extends AbstractInMemoryStorage<Friends> implements FriendsStorage {
}
