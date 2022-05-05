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
    @EqualsAndHashCode.Include private long id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
}
