package ru.yandex.practicum.filmorate.util;

/**
 * Вспомогательный класс, используемый для получения идентификаторов для объектов классов User и Film.
 */
public class IdGenerator {
    private static long id = 1;

    /**
     * Метод при каждом своем вызове возвращает новое значение идентификатора.
     * @return  значение идентификатора.
     */
    public static long getNextId() {
        return id++;
    }

    private IdGenerator(){}
}
