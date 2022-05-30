package ru.yandex.practicum.filmorate.exception;

/**
 *  Класс исключения, которое может быть сгенерировано если запрашиваемый элемент (фильм или пользователь) не найден в
 *  хранилище.
 */
public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException(String msg) {
        super(msg);
    }
}
