package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Класс реализует хранение объектов класса Film в ОЗУ.
 */
@Profile("in_memory_storage")
@Component
public class InMemoryFilmStorage extends AbstractInMemoryStorage<Film> implements FilmStorage {

    /**
     * Метод возвращает список всех фильмов, находящихся в хранилище в виде Collection<>. Если в хранилище нет ни одного
     * фильма, то метод вернет пустой список.
     *
     * @return список всех фильмов из хранилища; если в ранилище нет ни одного фильма, то метод вернет пустой список.
     */
    @Override
    public Collection<Film> getAllFilms() {
        return storage.values();
    }

    /**
     * Метод возвращает список наиболее популярных фильмов в виде Collection<>. Количество фильмов в списке определяется
     * size. Если в хранилище нет фильмов, то метод вернет пустой список.
     *
     * @param size размер списка фильмов;
     * @return список наиболее популярных фильмов; если в хранилище нет фильмов, то метод вернет пустой список.
     */
    @Override
    public Collection<Film> getTopFilms(int size) {
        return storage.values().stream().sorted((firstFilm, secondFilm) -> secondFilm.getRate() - firstFilm.getRate())
                .limit(size).collect(Collectors.toCollection(ArrayList::new));
    }
}
