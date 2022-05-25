package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

/**
 * Класс реализует хранение объектов класса Film в ОЗУ.
 */
@Component
public class InMemoryFilmStorage extends AbstractInMemoryStorage<Film, Long> implements FilmStorage {

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
}
