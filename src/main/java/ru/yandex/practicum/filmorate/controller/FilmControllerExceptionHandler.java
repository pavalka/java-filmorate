package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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

@Profile({"in_memory_storage", "in_db_storage"})
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
        logWarn(ex);
    }

    @ExceptionHandler(value = {UserNotFoundException.class, FilmNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleUserNotFoundException(ElementNotFoundException ex) {
        logWarn(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleOtherExceptions(Throwable ex) {
        logError(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleConstraintViolationException(ConstraintViolationException ex) {
        logWarn(ex);
    }

    private void logWarn(Throwable ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        logger.warn("{}::{}.{} : {}", ex.getClass().getName(), stackTrace[0].getClassName(),
               stackTrace[0].getMethodName(), ex.getMessage());
    }

    private void logError(Throwable ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        logger.error("{}::{}.{} : {}", ex.getClass().getName(), stackTrace[0].getClassName(),
                stackTrace[0].getMethodName(), ex.getMessage());
    }
}
