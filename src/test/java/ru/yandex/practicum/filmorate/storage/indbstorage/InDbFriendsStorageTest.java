package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("in_db_storage")
class InDbFriendsStorageTest {
    private final FriendsStorage friendsStorage;
    private final JdbcTemplate jdbcTemplate;
    private final static User USER_ONE = new User(1, "user1@email.ru", "user1", "name 1", LocalDate.of(1980, 2, 3));
    private final static User USER_TWO = new User(2, "user2@email.ru", "user2", "name 2", LocalDate.of(1990, 6, 24));
    private final static User USER_THREE = new User(3, "user3@email.ru", "user3", "name 3", LocalDate.of(1995, 1, 1));

    private void addUsersToDb() {
        jdbcTemplate.update("INSERT INTO users VALUES (1, 'user1@email.ru', 'user1', 'name 1', DATE '1980-02-03')");
        jdbcTemplate.update("INSERT INTO users VALUES (2, 'user2@email.ru', 'user2', 'name 2', DATE '1990-06-24')");
    }

    private void addFriendsToDb() {
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (1, 2)");
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (2, 1)");
    }

    @AfterEach
    public void deleteFriendsFromDb() {
        jdbcTemplate.update("DELETE FROM users");
    }

    @Autowired
    public InDbFriendsStorageTest(@Qualifier("inDbFriendsStorage") FriendsStorage friendsStorage,
                                  JdbcTemplate jdbcTemplate) {
        this.friendsStorage = friendsStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    void addFriendShouldAddFriend() {
        addUsersToDb();
        Assertions.assertDoesNotThrow(() -> friendsStorage.addFriend(USER_ONE, USER_TWO));

        var friends = friendsStorage.getFriends(USER_ONE);

        Assertions.assertEquals(1, friends.size());
        Assertions.assertTrue(friends.contains(USER_TWO));
    }

    @Test
    void deleteFriendShouldDeleteFriend() {
        addUsersToDb();
        addFriendsToDb();
        Assertions.assertDoesNotThrow(() -> friendsStorage.deleteFriend(USER_TWO, USER_ONE));

        var friends = friendsStorage.getFriends(USER_TWO);

        Assertions.assertTrue(friends.isEmpty());
    }

    @Test
    void deleteFriendShouldNotDeleteFriendWhenUserIsWrong() {
        addUsersToDb();
        addFriendsToDb();
        Assertions.assertDoesNotThrow(() -> friendsStorage.deleteFriend(USER_THREE, USER_TWO));

        var friends = friendsStorage.getFriends(USER_ONE);

        Assertions.assertEquals(1, friends.size());
        Assertions.assertTrue(friends.contains(USER_TWO));
        friends = friendsStorage.getFriends(USER_TWO);
        Assertions.assertEquals(1, friends.size());
        Assertions.assertTrue(friends.contains(USER_ONE));
    }

    @Test
    void deleteFriendShouldNotDeleteFriendWhenFriendIsWrong() {
        addUsersToDb();
        addFriendsToDb();

        Assertions.assertDoesNotThrow(() -> friendsStorage.deleteFriend(USER_ONE, USER_THREE));

        var friends = friendsStorage.getFriends(USER_ONE);

        Assertions.assertEquals(1, friends.size());
        Assertions.assertTrue(friends.contains(USER_TWO));
        friends = friendsStorage.getFriends(USER_TWO);
        Assertions.assertEquals(1, friends.size());
        Assertions.assertTrue(friends.contains(USER_ONE));
    }

    @Test
    void getFriendsShouldReturnEmptyCollection() {
        var friends = Assertions.assertDoesNotThrow(() -> friendsStorage.getFriends(USER_ONE));

        Assertions.assertTrue(friends.isEmpty());
    }

    @Test
    void getFriendsShouldReturnFriends() {
        addUsersToDb();
        addFriendsToDb();

        var friends = Assertions.assertDoesNotThrow(() -> friendsStorage.getFriends(USER_ONE));

        Assertions.assertEquals(1, friends.size());
        Assertions.assertTrue(friends.contains(USER_TWO));
    }
}