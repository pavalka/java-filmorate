package ru.yandex.practicum.filmorate.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import ru.yandex.practicum.filmorate.storage.indbstorage.InDbUserStorage;

/**
 * Класс создает бины IdGenerator.
 */
@Configuration
public class IdGeneratorBean {
    @Bean
    @Scope("prototype")
    @Profile("in_memory_storage")
    public IdGenerator generalIdGenerator() {
        return new IdGenerator();
    }

    @Bean
    @Scope("prototype")
    @Profile("in_db_prototype")
    public IdGenerator dbIdGenerator(InDbUserStorage userStorage) {
        IdGenerator idGenerator = new IdGenerator();
        idGenerator.setId(userStorage.getMaxUserId().orElse(1L));
        return idGenerator;
    }
}
