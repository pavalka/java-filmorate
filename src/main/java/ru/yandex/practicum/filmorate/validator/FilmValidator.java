package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

/**
 * Данный класс выполняет проверку объектов класса {@link Film} на соответствие заданным условиям:
 *  - название не может быть пустым;
 *  - максимальная длина описания = 200 символов;
 *  - дата релиза - не раньше 28.12.1895;
 *  - продолжительность фильма > 0;
 */
public class FilmValidator {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate MIN_FILM_DATE = LocalDate.of(1895, 12, 28);

    /**
     * Метод выполняет проверку фильма film на соответствие заданным условиям. Если film удовлетворяет этим условиям, то
     * метод вернет true, в противном случае - false.
     *
     * @param film  объекта класса {@link Film}, который нужно проверить;
     * @return  возвращаемое значение true если film удовлетворяет заданным условиям и false - в противном случае.
     */
    public static boolean validate(Film film) {
        return !(film.getName().isBlank() || film.getDescription().isBlank() || film.getDescription().length()
                > FilmValidator.MAX_DESCRIPTION_LENGTH || film.getReleaseDate().isBefore(MIN_FILM_DATE)
                || film.getDuration() < 0);
    }

    private FilmValidator(){}
}
