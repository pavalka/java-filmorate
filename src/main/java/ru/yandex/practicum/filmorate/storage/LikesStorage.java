package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface LikesStorage {
    boolean addLike(Film film, User user);
    boolean deleteLike(Film film, User user);
    Collection<User> getUsersByFilmId(long filmId);
}
