package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.logger.FilmControllerLogger;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmNotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserNotFoundException;

import javax.validation.constraints.Positive;
import java.util.Collection;

@Profile({"in_memory_storage", "in_db_storage"})
@Validated
@RestController
public class FilmController {
    private static final String FILMS_PATH = "/films";

    private final FilmService filmService;
    private final Logger logger;


    public FilmController(FilmService filmService, FilmControllerLogger loggerProvider) {
        this.filmService = filmService;
        this.logger = loggerProvider.getLogger();
    }

    /**
     * Метод обрабатывает эндпоинт GET "/films" и возвращает список всех фильмов.
     *
     * @return  Возвращает список всех фильмов или пустой список если в хранилище нет ни одного фильма.
     */
    @GetMapping(FILMS_PATH)
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    /**
     * Метод обрабатывает эндпоинт POST "/films", добавляя фильм в хранилище. После этого метод возвращает добавленный
     * фильм. Если фильм не соответствует заданным условия, то метод генерирует исключение {@link ValidationException}.
     *
     * @param newFilm   фильм, который нужно добавить в хранилище;
     * @return  фильм, добавленный в хранилище (newFilm);
     * @throws ValidationException  генерируется если фильм не соответствует заданным условиям;
     */
    @PostMapping(FILMS_PATH)
    public Film addFilm(@RequestBody Film newFilm) {
        newFilm = filmService.addFilm(newFilm);
        logger.info("addFilm: добавлен фильм с id = {}", newFilm.getId());
        return newFilm;
    }

    /**
     * Метод обновляет объект newFilm в хранилище.
     *
     * @param newFilm   фильм, который нужно обновить;
     * @return  возвращает обновленный фильм (newFilm);
     * @throws ValidationException  генерируется если фильм не соответствует заданным условичм;
     * @throws FilmNotFoundException    генерируется если фильм newFilm не найден в хранилище;
     */
    @PutMapping(FILMS_PATH)
    public Film updateFilm(@RequestBody Film newFilm) {
        filmService.updateFilm(newFilm);
        logger.info("updateFilm: обновлен фильм с id = {}", newFilm.getId());
        return newFilm;
    }

    /**
     * Метод возвращает фильм с идентификатором id.
     *
     * @param id    идентификатор фильма;
     * @return  фильм с идентификатором id;
     * @throws FilmNotFoundException    генерируется если фильм с идентификатором id не найден в хранилище;
     */
    @GetMapping(FILMS_PATH + "/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmService.getFilm(id);
    }

    /**
     * Метод добавляет лайк пользователя с идентификатором userId фильму с идентификатором filmId.
     *
     * @param filmId    идентификатор фильма;
     * @param userId    идентификатор пользователя;
     * @throws FilmNotFoundException    генерируется если фильм с идентификатором filmId не найден;
     * @throws UserNotFoundException    генерируется если пользователь с идентификатором userId не найден;
     */
    @PutMapping(FILMS_PATH + "/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long filmId, @PathVariable long userId) {
        filmService.addLike(filmId, userId);
        logger.info("addLike: Добавлен лайк фильму {} от пользователя {}", filmId, userId);
    }

    /**
     * Метод удаляет лайк пользователя с идентификатором  userId у фильма с идентификатором filmId.
     *
     * @param filmId    идентификатор фильма;
     * @param userId    идентификатор пользователя;
     * @throws FilmNotFoundException    генерируется если фильм с идентификатором filmId не найден в хранилище;
     * @throws UserNotFoundException    генерируется если пользователь с идентификатором userId не найден в хранилище;
     */
    @DeleteMapping(FILMS_PATH + "/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long filmId, @PathVariable long userId) {
        filmService.deleteLike(filmId, userId);
        logger.info("deleteLike: удален лайк пользователя {} у фильма {}", userId, filmId);
    }

    /**
     * Метод возвращает список фильмов, отсортированных по убыванию количества лайков. Количество фильмов в этом списке
     * определяется параметром count. Если count не задано в запросе, то значение count задается равным 10. Если в
     * хранилище фильмов меньше чем count, то метод вернет список всех фильмов.
     *
     * @param count размер возвращаемого списка фильмов;
     * @return  список фильмов, отсортированных по убыванию количества лайков;
     */
    @GetMapping(FILMS_PATH + "/popular")
    public Collection<Film> getPopularFilms(@Positive @RequestParam(defaultValue = "10") int count) {
        return filmService.getTopNFilms(count);
    }
}
