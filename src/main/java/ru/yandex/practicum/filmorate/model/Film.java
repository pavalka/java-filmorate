package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Класс, объекты которого представляют в программе фильмы.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class Film {
    @EqualsAndHashCode.Include private long id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
}
