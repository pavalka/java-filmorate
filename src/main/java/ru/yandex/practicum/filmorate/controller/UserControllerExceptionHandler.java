package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.logger.UserControllerLogger;
import ru.yandex.practicum.filmorate.service.UserNotFoundException;
import ru.yandex.practicum.filmorate.service.UserWithSameEmailException;

@RestControllerAdvice(assignableTypes = {UserController.class})
public class UserControllerExceptionHandler {
    private final Logger logger;

    @Autowired
    public UserControllerExceptionHandler(UserControllerLogger userController) {
        logger = userController.getLogger();
    }

    @ExceptionHandler(value = {ValidationException.class, UserWithSameEmailException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleValidationException(Exception ex) {
        logger.warn(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleUserNotFoundException(UserNotFoundException ex) {
        logger.warn(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleOtherExceptions(Throwable ex) {
        logger.error(ex.getMessage());
    }
}
