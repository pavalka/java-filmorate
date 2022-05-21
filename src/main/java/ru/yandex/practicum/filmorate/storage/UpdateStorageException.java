package ru.yandex.practicum.filmorate.storage;

/**
 * Исключение, генерируемое при ошибке обновления данных в хранилище.
 */
public class UpdateStorageException extends Exception {
    public UpdateStorageException(String msg) {
        super(msg);
    }
}
