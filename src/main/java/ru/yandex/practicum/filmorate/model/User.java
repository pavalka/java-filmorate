package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Класс, объекты которого представляют в программе пользователей.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
public class User extends Id{
    @EqualsAndHashCode.Include private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        if (name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }
}
