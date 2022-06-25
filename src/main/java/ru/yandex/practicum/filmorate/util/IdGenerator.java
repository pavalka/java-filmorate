package ru.yandex.practicum.filmorate.util;

/**
 * Вспомогательный класс, используемый для получения идентификаторов для объектов классов User и Film.
 */
public class IdGenerator {
    private long id;

    /**
     * Метод при каждом своем вызове возвращает новое значение идентификатора.
     * @return  значение идентификатора.
     */
    public long getNextId() {
        return ++id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
