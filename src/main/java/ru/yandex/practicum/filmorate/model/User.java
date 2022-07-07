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
    private final LocalDate birthday;
    private String name;


    public User(String email, String login,  LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = login;
        this.birthday = birthday;
    }

    public void setName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }
}
