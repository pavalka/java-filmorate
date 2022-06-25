package ru.yandex.practicum.filmorate.logger;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;

/**
 * Класс создает логгер для контроллера {@link UserController}
 */
@Component
public class UserControllerLogger extends GeneralLogger {
    public UserControllerLogger() {
        super(UserController.class);
    }
}
