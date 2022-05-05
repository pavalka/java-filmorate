package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

/**
 * Данный класс проверяет объекты класса {@link ru.yandex.practicum.filmorate.model.User} на соответствие заданным
 * условиям:
 *  - email не может быть пустым и должен содержать @;
 *  - логин не может быть пустым и содержать пробелы;
 *  - дата рождения не может быть в будущем.
 */
public class UserValidator {
    private static final String SPECIAL_SYMBOL = "@";
    private static final String SPACE_SYMBOL = " ";

    /**
     * Мето проверяет соответствие пользователя user на соответствие заданным условиям. Метод возвращает true если user
     *  удовлетворяет этим условиям и false - в противном случае.
     *
     * @param user  объект класса User, который нужно проверить;
     * @return  true - если user удовлетворяет условиям и false - в противном случае.
     */
    public static boolean validate(User user) {
        LocalDate currentDate = LocalDate.now();

        return !(user.getEmail().isBlank() || !user.getEmail().contains(SPECIAL_SYMBOL) || user.getLogin().isBlank()
               || !user.getLogin().contains(SPACE_SYMBOL) || user.getBirthday().isAfter(currentDate));
    }
}
