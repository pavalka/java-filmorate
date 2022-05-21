package ru.yandex.practicum.filmorate.storage;

/**
 * Исключение, генерируемое при ошибке добавления данных в хранилище.
 */
public class AddToStorageException extends Exception {
    public AddToStorageException(String msg) {
        super(msg);
    }
}
