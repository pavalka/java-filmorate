package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.IdGenerator;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {
    private static final String FILMS_PATH = "/films";

    private final Map<Long, Film> films;

    public FilmController() {
        this.films = new HashMap<>();
    }

    @GetMapping(FILMS_PATH)
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping(FILMS_PATH)
    public void addFilm(@RequestBody Film newFilm) throws ValidationException {
        if (!FilmValidator.validate(newFilm)) {
            throw new ValidationException("Один из параметроф фильма не соответствует заданным условиям.");
        }

        newFilm.setId(IdGenerator.getNextId());
        films.put(newFilm.getId(), newFilm);
    }

    @PutMapping(FILMS_PATH)
    public void updateFilm(@RequestBody Film newFilm) {
        films.put(newFilm.getId(), newFilm);
    }
}
