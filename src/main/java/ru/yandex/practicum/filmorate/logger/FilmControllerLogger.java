package ru.yandex.practicum.filmorate.logger;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;

/**
 * Класс создает логгер для контроллера {@link FilmController}
 */
@Profile({"in_memory_storage", "in_db_storage"})
@Component
public class FilmControllerLogger extends GeneralLogger {
    public FilmControllerLogger() {
        super(FilmController.class);
    }
}
