package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UpdateException;
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
    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);
    private static final IdGenerator idGenerator = new IdGenerator();

    private final Map<Long, Film> films;

    public FilmController() {
        this.films = new HashMap<>();
    }

    @GetMapping(FILMS_PATH)
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping(FILMS_PATH)
    public Film addFilm(@RequestBody Film newFilm) throws ValidationException {
        if (newFilm == null || !FilmValidator.validate(newFilm)) {
            logger.warn("addFilm: запрос не соответствует условиям. film = {}", newFilm);
            throw new ValidationException("Параметры фильма не соответствует заданным условиям.");
        }

        newFilm.setId(idGenerator.getNextId());
        films.put(newFilm.getId(), newFilm);
        logger.info("addFilm: добавлен фильм с id = {}", newFilm.getId());
        return newFilm;
    }

    @PutMapping(FILMS_PATH)
    public Film updateFilm(@RequestBody Film newFilm) throws ValidationException {
        if (newFilm == null) {
            logger.warn("updateFilm: запрос не соответствует условиям. film = null");
            throw new ValidationException("Пустой запрос.");
        }

        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            logger.info("updateFilm: обновлен фильм с id = {}", newFilm.getId());
        } else {
            logger.warn("updateFilm: фильм не найден. film = {}", newFilm);
            throw new UpdateException("Неверный id фильма");
        }

        return newFilm;
    }
}
