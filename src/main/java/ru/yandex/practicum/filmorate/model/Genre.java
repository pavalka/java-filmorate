package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Класс описывает жанр фильма.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value
public class Genre {
    @EqualsAndHashCode.Include
    int id;
    String name;
}
