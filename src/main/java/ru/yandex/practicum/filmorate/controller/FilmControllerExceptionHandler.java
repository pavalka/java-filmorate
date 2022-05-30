package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.logger.FilmControllerLogger;
import ru.yandex.practicum.filmorate.service.FilmNotFoundException;
import ru.yandex.practicum.filmorate.service.UserNotFoundException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice(assignableTypes = {FilmController.class})
public class FilmControllerExceptionHandler {
    private final Logger logger;

    @Autowired
    public FilmControllerExceptionHandler(FilmControllerLogger filmLogger) {
        logger = filmLogger.getLogger();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleValidationException(ValidationException ex) {
        logger.warn(ex.getMessage());
    }

    @ExceptionHandler(value = {UserNotFoundException.class, FilmNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleUserNotFoundException(ElementNotFoundException ex) {
        logger.warn(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleOtherExceptions(Throwable ex) {
        logger.error(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleConstraintViolationException(ConstraintViolationException ex) {
        logger.info(ex.getMessage());
    }
}
