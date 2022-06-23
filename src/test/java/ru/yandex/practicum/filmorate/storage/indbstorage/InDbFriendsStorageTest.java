package ru.yandex.practicum.filmorate.storage.indbstorage;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
class InDbFriendsStorageTest {
    private final FriendsStorage friendsStorage;
    private final JdbcTemplate jdbcTemplate;

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
        Assertions.assertDoesNotThrow(() -> friendsStorage.addFriend(1, 2));

        Optional<Friends> wrappedFriends = friendsStorage.get(1);

        Assertions.assertTrue(wrappedFriends.isPresent());
        Assertions.assertEquals(1, wrappedFriends.get().getFriendsId().size());
        Assertions.assertTrue(wrappedFriends.get().getFriendsId().contains(2L));
    }

    @Test
    void deleteFriendShouldDeleteFriend() {
        addUsersToDb();
        addFriendsToDb();

        Assertions.assertDoesNotThrow(() -> friendsStorage.deleteFriend(2, 1));

        Optional<Friends> wrappedFriends = friendsStorage.get(2);

        Assertions.assertTrue(wrappedFriends.isEmpty());
    }

    @Test
    void deleteFriendShouldNotDeleteFriendWhenUserIsWrong() {
        addUsersToDb();
        addFriendsToDb();

        Assertions.assertDoesNotThrow(() -> friendsStorage.deleteFriend(3, 2));

        Optional<Friends> wrappedFriends = friendsStorage.get(1);

        Assertions.assertTrue(wrappedFriends.isPresent());
        Assertions.assertEquals(1, wrappedFriends.get().getFriendsId().size());
        wrappedFriends = friendsStorage.get(2);
        Assertions.assertTrue(wrappedFriends.isPresent());
        Assertions.assertEquals(1, wrappedFriends.get().getFriendsId().size());
    }

    @Test
    void deleteFriendShouldNotDeleteFriendWhenFriendIsWrong() {
        addUsersToDb();
        addFriendsToDb();

        Assertions.assertDoesNotThrow(() -> friendsStorage.deleteFriend(1, 3));

        Optional<Friends> wrappedFriends = friendsStorage.get(1);

        Assertions.assertTrue(wrappedFriends.isPresent());
        Assertions.assertEquals(1, wrappedFriends.get().getFriendsId().size());
        wrappedFriends = friendsStorage.get(2);
        Assertions.assertTrue(wrappedFriends.isPresent());
        Assertions.assertEquals(1, wrappedFriends.get().getFriendsId().size());
    }

    @Test
    void put() {
        addUsersToDb();

        Optional<Friends> wrappedFriends = friendsStorage.get(1);

        Assertions.assertTrue(wrappedFriends.isEmpty());
        wrappedFriends = friendsStorage.get(2);
        Assertions.assertTrue(wrappedFriends.isEmpty());

        Friends friends = new Friends(1);

        friends.addFriend(2);
        Assertions.assertDoesNotThrow(() -> friendsStorage.put(friends));
        wrappedFriends = friendsStorage.get(1);
        Assertions.assertTrue(wrappedFriends.isPresent());
        Assertions.assertEquals(1, wrappedFriends.get().getFriendsId().size());
        Assertions.assertTrue(wrappedFriends.get().getFriendsId().contains(2L));
        wrappedFriends = friendsStorage.get(2);
        Assertions.assertTrue(wrappedFriends.isEmpty());
    }

    @Test
    void getShouldReturnEmptyOptional() {
        Optional<Friends> wrappedFriends = Assertions.assertDoesNotThrow(() -> friendsStorage.get(1));

        Assertions.assertTrue(wrappedFriends.isEmpty());
    }

    @Test
    void getShouldReturnFriends() {
        addUsersToDb();
        addFriendsToDb();

        Optional<Friends> wrappedFriends = Assertions.assertDoesNotThrow(() -> friendsStorage.get(1));

        Assertions.assertTrue(wrappedFriends.isPresent());
        Assertions.assertEquals(1, wrappedFriends.get().getFriendsId().size());
        Assertions.assertTrue(wrappedFriends.get().getFriendsId().contains(2L));
    }
}