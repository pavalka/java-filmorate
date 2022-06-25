package ru.yandex.practicum.filmorate.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import ru.yandex.practicum.filmorate.storage.indbstorage.InDbFilmStorage;
import ru.yandex.practicum.filmorate.storage.indbstorage.InDbUserStorage;

/**
 * Класс создает бины IdGenerator.
 */
@Configuration
public class IdGeneratorBean {
    @Bean(name = {"userIdGenerator", "filmIdGenerator"})
    @Scope("prototype")
    @Profile("in_memory_storage")
    public IdGenerator idGenerator() {
        return new IdGenerator();
    }

    @Bean(name = "userIdGenerator")
    @Profile("in_db_storage")
    public IdGenerator userIdGenerator(InDbUserStorage userStorage) {
        IdGenerator idGenerator = new IdGenerator();
        idGenerator.setId(userStorage.getMaxUserId().orElse(1L));
        return idGenerator;
    }

    @Bean(name = "filmIdGenerator")
    @Profile("in_db_storage")
    public IdGenerator filmIdGenerator(InDbFilmStorage filmStorage) {
        IdGenerator idGenerator = new IdGenerator();
        idGenerator.setId(filmStorage.getMaxFilmId().orElse(1L));
        return idGenerator;
    }
}

