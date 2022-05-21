package ru.yandex.practicum.filmorate.storage;

/**
 * Исключение, генерируемое при возникновении ошибке в процессе удаления данных из хранилища.
 */
public class DeleteFromStorageException extends Exception {
    public DeleteFromStorageException(String msg) {
        super(msg);
    }
}
