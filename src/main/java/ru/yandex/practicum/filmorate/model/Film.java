package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Класс, объекты которого представляют в программе фильмы.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Film extends Id{
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private int rate;
}
