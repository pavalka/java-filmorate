package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс, отвечающий за логику выполнения операций над пользователями.
 */
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;
    private final IdGenerator idGenerator;

    /**
     * Конструктор класса.
     *  @param userStorage   хранилище пользователей;
     * @param friendsStorage    хранилище списка друзей;
     * @param idGenerator   генератор идентификаторов;
     */
    @Autowired
    public UserService(UserStorage userStorage, FriendsStorage friendsStorage, IdGenerator idGenerator) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
        this.idGenerator = idGenerator;
    }

    /**
     * Метод возвращает список всех пользователей в хранилище. Если в хранилище нет ни одного пользователя, то метод
     * вернет пустой список.
     *
     * @return  список всех пользователей; если в хранилище нет ни одного пользователя, то метод вернет пустой список.
     */
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    /**
     * Метод добавляет пользователя user в хранилище пользователей. При этом происходит проверка удовлетворяет ли
     * user заданным условиям. Если user = null или не удовлетворяет заданным условиям, то генерируется исключение
     * {@link ValidationException}. Также метод проверяет есть ли в хранилище пользователь с таким же email как у user.
     * Если такой пользователь в хранилище есть, то метод сгенерирует исключение {@link UserWithSameEmailException}.
     *
     * @param user  пользователь, которого нужно добавить в хранилище;
     * @throws ValidationException  генерируется если user = null или не удовлетворяет заданным условиям;
     * @throws UserWithSameEmailException   генерируется если в хранилище есть пользователь с email = user.email;
     * @return  пользователь с присвоенным ему идентификатором.
     */
    public User addUser(User user) {
        if (user == null || !UserValidator.validate(user)) {
            throw new ValidationException("Параметры пользователя не соответствуют заданным условиям");
        }

        if (!userStorage.isUserPresent(user)) {
            user.setId(idGenerator.getNextId());
            userStorage.put(user);
        } else {
            throw new UserWithSameEmailException(String.format("Пользователь с email = %s уже существует " +
                                                 "в хранилище.", user.getEmail()));
        }

        return user;
    }

    /**
     * Метод проверяет, что пользователь user удовлетворяет заданным условиям и обновляет данного пользователя в
     * хранилище пользователей.
     *
     * @param user  пользователь, которого нужно обновить;
     * @throws ValidationException  генерируется если user = null или не удовлетворяет заданным условиям;
     * @throws UserNotFoundException   генерируется, если user нет в хранилище;
     */
    public void updateUser(User user) {
        if (user == null || !UserValidator.validate(user)) {
            throw new ValidationException("Параметры пользователя не соответствуют заданным условиям");
        }

        getUser(user.getId());
        userStorage.put(user);
    }

    /**
     * Метод возвращает полдьзователя с идентификатором равным userId. Если пользователя с таким идентификатором нет в
     * хранилище, то метод сгенерирует исключение {@link UserNotFoundException}.
     *
     * @param userId    идентификатор пользователя;
     * @return  пользователь с идентификатором раным userId;
     * @throws UserNotFoundException    генерируется если пользователя с идентификатором userId нет в хранилище;
     */
    public User getUser(long userId) {
        return userStorage.get(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("Пользователь с id = %d не найден.", userId))
                );
    }

    /**
     * Метод добавляет пользователя с идентификатором userIdTwo в список друзей пользователя с идентификтором userIdOne, а
     * пользователя с идентификатором userIdOne в список друзей пользователя userIdTwo. При этом происходит проверка, что
     * пользователи с идентификаторами userIdOne и userIdTwo действительно есть в хранилище. Если какого-либо из
     * пользователей нет в хранилище, то метод сгенерирует исключение {@link UserNotFoundException}.
     *
     * @param userIdOne    идентификатор пользователя, которому нужно добавить друга;
     * @param userIdTwo  идентификатор пользователя, который добавляется в друзья;
     * @throws UserNotFoundException    генерируется если какой-либо из пользователей с идентификаторами userIdOne или
     *                                  userIdTwo не найден в хранилище.
     */
    public void addFriend(long userIdOne, long userIdTwo) {
        getUser(userIdOne);
        getUser(userIdTwo);

        Friends friends = friendsStorage.get(userIdOne).orElseGet(() -> new Friends(userIdOne));

        friends.addFriend(userIdTwo);
        friendsStorage.put(friends);
        friends = friendsStorage.get(userIdTwo).orElseGet(() -> new Friends(userIdTwo));
        friends.addFriend(userIdOne);
        friendsStorage.put(friends);
    }

    /**
     * Метод удаляет пользователя с идентификатором userIdTwo из списка друзей пользователя с идентификатором userIdOne.
     * А так же удаляет пользователя с идентификатором userIdOne из списка друзей пользователя с идентификатором
     * userIdTwo. При этом происходит проверка того, что оба пользователя действиьельно есть в хранилище. Если одного из
     * пользователей нет в хранилище, то метод сгенерирует исключение {@link UserNotFoundException}.
     *
     * @param userIdOne идентификатор первого пользователя;
     * @param userIdTwo идентификатор второго пользователя;
     * @throws UserNotFoundException    генерируется если одного из пользователей нет в хранилище;
     */
    public void deleteFriend(long userIdOne, long userIdTwo) {
        getUser(userIdOne);
        getUser(userIdTwo);

        Optional<Friends> wrappedFriends = friendsStorage.get(userIdOne);
        Friends friends;

        if (wrappedFriends.isEmpty()) {
            return;
        }

        friends = wrappedFriends.get();
        friends.deleteFriend(userIdTwo);
        friendsStorage.put(friends);
        wrappedFriends = friendsStorage.get(userIdTwo);

        if (wrappedFriends.isPresent()) {
            friends = wrappedFriends.get();
            friends.deleteFriend(userIdOne);
            friendsStorage.put(friends);
        }
    }

    /**
     * Метод возвращает список общих друзей для пользователей с идентификаторами userIdOne и userIdTwo. При этом метод
     * проверяет, что пользователи с этими идентификатороми действительно есть в хранилище. Если одного или обоих
     * пользователей нет в хранилище, то метод сгенерирует исключение {@link UserNotFoundException}.
     *
     * @param userIdOne идентификатор первого пользователя;
     * @param userIdTwo идентификатор второго пользователя;
     * @return  список общих друзей; если общих друзей нет, то метод вернет пустой список;
     * @throws UserNotFoundException    генерируется если одного или обоих пользователей нет в хранилище;
     */
    public Collection<User> getCommonFriends(long userIdOne, long userIdTwo) {
        getUser(userIdOne);
        getUser(userIdTwo);

        Optional<Friends> wrappedFriendsOne = friendsStorage.get(userIdOne);
        Optional<Friends> wrappedFriendsTwo = friendsStorage.get(userIdTwo);

        if (wrappedFriendsOne.isEmpty() || wrappedFriendsTwo.isEmpty()) {
            return new ArrayList<>();
        }

        Set<Long> friendsOne = wrappedFriendsOne.get().getFriendsId();
        Set<Long> friendsTwo = wrappedFriendsTwo.get().getFriendsId();

        if (friendsOne.size() > friendsTwo.size()) {
            friendsOne = wrappedFriendsTwo.get().getFriendsId();
            friendsTwo = wrappedFriendsOne.get().getFriendsId();
        }

        return friendsOne.stream().filter(friendsTwo::contains).map(userId -> userStorage.get(userId).get())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Метод возвращает список друзей пользователя с идентификатором userId. При этом происходит проверка, что этот
     * пользователь действительно есть в хранилище. Если пользователя с идентификатором userId нет в хранилище, то
     * метод сгенерирует исключение {@link UserNotFoundException}. Если у этого пользователя нет друзей, то метод вернет
     * пустой список.
     *
     * @param userId    идентификатор пользователя;
     * @return  список друзей пользователя; если у этого пользователя нет друзей, то метод вернет пустой список;
     * @throws UserNotFoundException    генерируется если пользователя с идентификатором userId нет в хранилище;
     */
    public Collection<User> getUserFriends(long userId) {
        getUser(userId);

        Optional<Friends> wrappedFriends = friendsStorage.get(userId);

        if (wrappedFriends.isEmpty()) {
            return new ArrayList<>();
        }

        return wrappedFriends.get().getFriendsId().stream().map(uid -> userStorage.get(uid).get())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
