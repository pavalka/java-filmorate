package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс описывает лайки, связанные с фильмом. Фильм и пользователи, которые отметили его задаеются их идентификаторами.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@RequiredArgsConstructor
public class Likes {
    @Getter
    @EqualsAndHashCode.Include
    private final long filmId;
    private final Set<Long> usersId = new HashSet<>();

    /**
     * Метод добавляет фильму лайк от пользователя с идентификатором userId.
     *
     * @param userId    идентификатор пользователя.
     */
    public void addLike(long userId) {
        this.usersId.add(userId);
    }

    /**
     * Метод удаляет у фильма лайк от пользователя с идентификатором userId.
     *
     * @param userId    идентификатор пользователя;
     */
    public void deleteLike(long userId) {
        this.usersId.remove(userId);
    }

    /**
     * Метод возвращает количество лайков.
     *
     * @return  количество лайков.
     */
    public int getLikes() {
        return usersId.size();
    }
}
