package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс описывает список друзей пользователя. Пользователь и его друзья задаются их идентификаторами.
 */
@Getter
@Setter
public class Friends extends Id{
    private final Set<Long> friendsId = new HashSet<>();

    public Friends(long id) {
        setId(id);
    }
    /**
     * Метод добавляет пользователя с идентификатором uid в список друзей.
     *
     * @param uid   идентификатор пользователя, которого нужно добавить в список друзей;
     */
    public void addFriend(long uid) {
        friendsId.add(uid);
    }

    /**
     * Метод удаляет пользователя с идентификатором uid из списка друзей.
     *
     * @param uid   идентификатор пользователя, которого нужно удалить из списка друзей;
     */
    public void deleteFriend(long uid) {
        friendsId.remove(uid);
    }
}
