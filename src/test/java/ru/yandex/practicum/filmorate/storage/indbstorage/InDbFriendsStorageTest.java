package ru.yandex.practicum.filmorate.storage.indbstorage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
class InDbFriendsStorageTest {
    private final FriendsStorage friendsStorage;
    private final JdbcTemplate jdbcTemplate;
    private final User userOne;
    private final User userTwo;
    private final User userThree;

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
        userOne = new User("user1@email.ru", "user1", LocalDate.of(1980, 2, 3));
        userOne.setId(1);
        userOne.setName("name 1");
        userTwo = new User("user2@email.ru", "user2", LocalDate.of(1990, 6, 24));
        userTwo.setId(2);
        userTwo.setName("name 2");
        userThree = new User("user3@email.ru", "user3", LocalDate.of(1995, 1, 1));
        userThree.setId(3);
        userThree.setName("name 3");
    }

    @Test
    void addFriendShouldAddFriend() {
        addUsersToDb();
        Assertions.assertDoesNotThrow(() -> friendsStorage.addFriend(userOne, userTwo));

        var friends = friendsStorage.getFriends(userOne);

        Assertions.assertEquals(1, friends.size());
        Assertions.assertTrue(friends.contains(userTwo));
    }

    @Test
    void deleteFriendShouldDeleteFriend() {
        addUsersToDb();
        addFriendsToDb();
        Assertions.assertDoesNotThrow(() -> friendsStorage.deleteFriend(userTwo, userOne));

        var friends = friendsStorage.getFriends(userTwo);

        Assertions.assertTrue(friends.isEmpty());
    }

    @Test
    void deleteFriendShouldNotDeleteFriendWhenUserIsWrong() {
        addUsersToDb();
        addFriendsToDb();
        Assertions.assertDoesNotThrow(() -> friendsStorage.deleteFriend(userThree, userTwo));

        var friends = friendsStorage.getFriends(userOne);

        Assertions.assertEquals(1, friends.size());
        Assertions.assertTrue(friends.contains(userTwo));
        friends = friendsStorage.getFriends(userTwo);
        Assertions.assertEquals(1, friends.size());
        Assertions.assertTrue(friends.contains(userOne));
    }

    @Test
    void deleteFriendShouldNotDeleteFriendWhenFriendIsWrong() {
        addUsersToDb();
        addFriendsToDb();

        Assertions.assertDoesNotThrow(() -> friendsStorage.deleteFriend(userOne, userThree));

        var friends = friendsStorage.getFriends(userOne);

        Assertions.assertEquals(1, friends.size());
        Assertions.assertTrue(friends.contains(userTwo));
        friends = friendsStorage.getFriends(userTwo);
        Assertions.assertEquals(1, friends.size());
        Assertions.assertTrue(friends.contains(userOne));
    }

    @Test
    void getFriendsShouldReturnEmptyCollection() {
        var friends = Assertions.assertDoesNotThrow(() -> friendsStorage.getFriends(userOne));

        Assertions.assertTrue(friends.isEmpty());
    }

    @Test
    void getFriendsShouldReturnFriends() {
        addUsersToDb();
        addFriendsToDb();

        var friends = Assertions.assertDoesNotThrow(() -> friendsStorage.getFriends(userOne));

        Assertions.assertEquals(1, friends.size());
        Assertions.assertTrue(friends.contains(userTwo));
    }
}