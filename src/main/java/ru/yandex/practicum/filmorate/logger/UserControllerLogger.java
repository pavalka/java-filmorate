package ru.yandex.practicum.filmorate.logger;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;

/**
 * Класс создает логгер для контроллера {@link UserController}
 */
@Profile({"in_memory_storage", "in_db_storage"})
@Component
public class UserControllerLogger extends GeneralLogger {
    public UserControllerLogger() {
        super(UserController.class);
    }
}
