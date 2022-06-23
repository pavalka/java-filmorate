package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Класс описывает рейтинг фильма.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value
public class Mpa {
    @EqualsAndHashCode.Include
    int id;
    String name;
}
