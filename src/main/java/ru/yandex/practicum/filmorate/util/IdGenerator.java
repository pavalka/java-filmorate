package ru.yandex.practicum.filmorate.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Вспомогательный класс, используемый для получения идентификаторов для объектов классов User и Film.
 */
@Component
@Scope("prototype")
public class IdGenerator {
    private long id;

    /**
     * Метод при каждом своем вызове возвращает новое значение идентификатора.
     * @return  значение идентификатора.
     */
    public long getNextId() {
        return id++;
    }

    public IdGenerator(){
        id = 1;
    }
}
