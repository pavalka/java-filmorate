package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Map;
import java.util.Set;

/**
 * Интерфейс для работы с таблицей film_genre.
 */
public interface FilmGenreStorage {
    /**
     * Метод возвращает набор жанров для заданного фильма. Если для данного фильма не задан ни один жанр, то метод
     * вернет пустой набор. Если в процессе работы произойдет ошибка, то метод сгенерирует исключение
     * {@link org.springframework.dao.DataAccessException}.
     *
     * @param filmId    идентификатор фильма;
     * @return  набор жанров для заданного фильма; если для данного фильма не задан ни один жанр, то метод вернет пустой
     *          набор.
     * @throws  org.springframework.dao.DataAccessException   будет сгенерировано если в процессе работы произойдет
     *          ошибка.
     */
    Set<Genre> getGenresForFilmByFilmId(long filmId);

    /**
     * Метод возвращает наборы жанров для всех фильмов в виде объекта класса Map. В качетсве ключа в Map выступает id
     * фильма. Если таблица film_genre пуста, то метод врент пустой объек класса Map. Если в процессе работы метода
     * произойдет ошибка, то будет сгенерировано исключение {@link org.springframework.dao.DataAccessException}.
     *
     * @return  наборы жанров для всех фильмов в виде объекта класса Map; если таблица film_genre пуста, то метод врент
     *          пустой объек класса Map.
     * @throws  org.springframework.dao.DataAccessException будет сгенерировано если в процессе работы метода произойдет
     *          ошибка.
     */
    Map<Long, Set<Genre>> getGenresForAllFilms();

    /**
     * Метод добавляет/обновляет список жанров genres для фильма с идентификатором filmId. Если в процессе работы метода
     * произойдет ошибка, то будет сгенерировано исключение {@link org.springframework.dao.DataAccessException}.
     *
     * @param filmId    идентификатор фильма;
     * @param genres    список жанров для фильма;
     * @throws org.springframework.dao.DataAccessException  генерируется, если в процессе работы метода произойдет ошибка
     */
    void putGenresForFilm(long filmId, Set<Genre> genres);
}
