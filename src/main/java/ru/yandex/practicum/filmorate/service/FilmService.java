package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.FilmIdGenerator;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;

/**
 * Класс, отвечающий за логику выполнения операций с фильмами.
 */
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmIdGenerator idGenerator;

    /**
     * Конструктор класса.
     *  @param filmStorage   хранилище фильмов;
     * @param userStorage   хранилище пользователей;
     */
    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, FilmIdGenerator idGenerator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.idGenerator = idGenerator;
    }

    /**
     * Метод возвращает список всех фильмов из хранилища. Если в хранилище нет ни одного фильма, то метод вернет пустой
     * список.
     *
     * @return  список всех фильмов из хранилища; если в хранилище нет ни одного фильма, то метод вернет пустой список.
     */
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    /**
     * Метод проверяет, что фильм film удовлетворяет заданным условиям, присваивает ему идентификатор и сохраняет в
     * хранилище.
     *
     * @param film  фильм, который нужно добавить в хранилище;
     * @throws ValidationException  генерируется если film = null или если film не удовлетворяет заданным условиям;
     * @return  фильм с присвоенным ему идентификатором.
     */
    public Film addFilm(Film film) {
        if (film == null || !FilmValidator.validate(film)) {
            throw new ValidationException("addFilm: Параметры фильма не соответствует заданным условиям.");
        }

        film.setId(idGenerator.getNextId());
        filmStorage.put(film.getId(), film);
        return film;
    }

    /**
     * Метод проверяет, что фильм film удовлетворяет заданным условиям и обновляет данный фильм в хранилище фильмов.
     *
     * @param film  фильм, который необходимо обновить;
     * @throws ValidationException  генерируется если film = null или если film не удовлетворяет заданным условиям;
     * @throws FilmNotFoundException    генерируется, если film нет в хранилище;
     */
    public void updateFilm(Film film) {
        if (film == null || !FilmValidator.validate(film)) {
            throw new ValidationException("updateFilm: Параметры фильма не соответствует заданным условиям.");
        }

        if (!filmStorage.isKeyPresent(film.getId())) {
            throw new FilmNotFoundException(String.format("updateFilm: Фильм с id = %d не найден в хранилище."
                                            , film.getId()));
        }
        filmStorage.put(film.getId(), film);
    }

    /**
     * Метод возвращает фильм с идентификатором filmId. Если фильм с таким идентификатором не найден в хранилище фильмов,
     * то метод сгенерирует исключение {@link FilmNotFoundException}.
     *
     * @param filmId    идентификатор фильма;
     * @return  фильм с идентификатором filmId;
     * @throws FilmNotFoundException    генерируется если фильм с идентификатором filmId не найден в хранилище фильмов;
     */
    public Film getFilm(long filmId) {
        return filmStorage.get(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("getFilm: Фильм с id = %d не найден", filmId))
                );
    }

    /**
     * Метод добавляет лайк фильму с идентификатором filmId от пользователя с идентификатором userId. При этом метод
     * проверяет, что указанные фильм и пользователь существуют в хранилищах. Если filmId нет в хранилище фильмов, то
     * метод сгенерирует исключение {@link FilmNotFoundException}. Если userId нет в хранилище пользователь, то будет
     * сгенерировано исключение {@link UserNotFoundException}.
     *   @param filmId    идентификатор фильма, которуму добавляется лайк;
     *  @param userId    идентификатор пользователя, который ставит лайк;
     */
    public void addLike(long filmId, long userId) {
        if (!userStorage.isKeyPresent(userId)) {
            throw new UserNotFoundException(String.format("addLike: Пользователь с id = %d не найден.", userId));
        }

        Film film = filmStorage.get(filmId).orElseThrow(() -> new FilmNotFoundException(String.format(
                                                              "addLike: Фильм с id = %d не найден.", filmId))
                                    );
        film.setRate(film.getRate() + 1);
        filmStorage.put(filmId, film);
    }

    /**
     * Метод удаляет лайк у фильма с идентификатором filmId от пользователя с идентификатором userId. При этом происходит
     * проверка, что указанные фильм и пользователь существуют в соответствующих хранилищах. Если filmId нет в хранилище
     * фильмов, то метод сгенерирует исключение {@link FilmNotFoundException}. Если userId нет в хранилище пользователей,
     * то будет сгенерировано исключение {@link UserNotFoundException}.
     *
     * @param filmId    идентификатор фильма, у котрого удаляется лайк;
     * @param userId    идентификатор пользователя, который удаляет лайк;
     * @throws FilmNotFoundException    генерируется, если filmId не найден в хранилище фильмов;
     * @throws UserNotFoundException    генерируется, если userId не найден в хранилище пользователей.
     */
    public void deleteLike(long filmId, long userId) {
        if (!userStorage.isKeyPresent(userId)) {
            throw new UserNotFoundException(String.format("deleteLike: Пользователь с id = %d не найден.", userId));
        }

        Film film = filmStorage.get(filmId).orElseThrow(() -> new FilmNotFoundException(String.format(
                                                              "deleteLike: Фильм с id = %d не найден.", filmId))
                                    );

        film.setRate(film.getRate() - 1);
        filmStorage.put(filmId, film);
    }

    /**
     * Метод возвращает список из size наиболее популярных фильмов. Если в хранилище менее size фильмов, то метод вернет
     * список из всех фильмов в хранилище. Если в хранилище нет ни одного фильма, то метод вернет пустой список.
     *
     * @param size  максимальное количество фильмов, которое вернет метод;
     * @return  список наиболее популярных фильмов; если в хранилище нет ни одного фильма, то метод вернет пустой список.
     */
    public Collection<Film> getTopNFilms(int size) {
        return filmStorage.getTopFilms(size);
    }
}
