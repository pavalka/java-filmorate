package ru.yandex.practicum.filmorate.logger;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;

/**
 * Класс создает логгер для контроллера {@link FilmController}
 */
@Component
public class FilmControllerLogger extends GeneralLogger {
    public FilmControllerLogger() {
        super(FilmController.class);
    }
}
