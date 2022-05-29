package ru.yandex.practicum.filmorate.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Базовый класс для создания и передачи логерра в бины.
 */
public class GeneralLogger {
    private final Logger logger;

    public GeneralLogger(Class<?> loggerOwner) {
        logger = LoggerFactory.getLogger(loggerOwner);
    }

    public Logger getLogger() {
        return logger;
    }
}
