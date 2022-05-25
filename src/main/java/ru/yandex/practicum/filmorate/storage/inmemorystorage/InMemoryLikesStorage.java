package ru.yandex.practicum.filmorate.storage.inmemorystorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class InMemoryLikesStorage extends AbstractInMemoryStorage<Likes, Long> implements LikesStorage {
    /**
     * Метод возвращает список наиболее популярных фильмов в виде Collection<>. Количество фильмов в списке определяется
     * size. Если в хранилище нет фильмов, то метод вернет пустой список.
     *
     * @param size размер списка фильмов;
     * @return список наиболее популярных фильмов; если в хранилище нет фильмов, то метод вернет пустой список.
     */
    @Override
    public Collection<Likes> getTopFilms(int size) {
        return storage.values().stream().sorted((first, second) -> Integer.compare(second.getLikes(), first.getLikes()))
                .limit(size).collect(Collectors.toCollection(ArrayList::new));
    }
}
