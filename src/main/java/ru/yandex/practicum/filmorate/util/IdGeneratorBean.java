package ru.yandex.practicum.filmorate.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.filmorate.storage.indbstorage.InDbFilmStorage;
import ru.yandex.practicum.filmorate.storage.indbstorage.InDbUserStorage;

/**
 * Класс создает бины IdGenerator.
 */
@Configuration
public class IdGeneratorBean {
    @Bean(name = "userIdGenerator")
    public IdGenerator userIdGenerator(InDbUserStorage userStorage) {
        IdGenerator idGenerator = new IdGenerator();
        idGenerator.setId(userStorage.getMaxUserId().orElse(1L));
        return idGenerator;
    }

    @Bean(name = "filmIdGenerator")
    public IdGenerator filmIdGenerator(InDbFilmStorage filmStorage) {
        IdGenerator idGenerator = new IdGenerator();
        idGenerator.setId(filmStorage.getMaxFilmId().orElse(1L));
        return idGenerator;
    }
}

