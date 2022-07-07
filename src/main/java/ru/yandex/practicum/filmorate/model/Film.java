package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

/**
 * Класс, объекты которого представляют в программе фильмы.
 */
@Getter
@Setter
//@RequiredArgsConstructor
public class Film extends Id{
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private int rate;
    private Mpa mpa;
    private Set<Genre> genres;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

//    public Film(long id, String name, String description, LocalDate releaseDate, int duration, int rate, Mpa ratings,
//                Set<Genre> genres) {
//        this(name, description, releaseDate, duration);
//        this.rate = rate;
//        this.ratings = ratings;
//        this.genres = genres;
//        setId(id);
//    }
}
