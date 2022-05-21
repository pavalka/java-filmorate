package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Класс, объекты которого представляют в программе пользователей.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class User {
    private long id;
    @EqualsAndHashCode.Include private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;
}
