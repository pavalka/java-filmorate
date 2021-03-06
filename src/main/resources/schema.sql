CREATE TABLE IF NOT EXISTS ratings (
    rating_id INTEGER GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(12) NOT NULL,
    CONSTRAINT pk_ratings PRIMARY KEY(rating_id)
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id INTEGER GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(20) NOT NULL,
    CONSTRAINT pk_genres PRIMARY KEY(genre_id)
);

CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    duration INTEGER NOT NULL,
    likes INTEGER NOT NULL DEFAULT 0,
    rating_id INTEGER NOT NULL,
    release_date DATE NOT NULL,
    CONSTRAINT pk_films PRIMARY KEY(film_id),
    CONSTRAINT chk_films_duration CHECK(duration>0),
    CONSTRAINT fk_films_rating_id FOREIGN KEY(rating_id) REFERENCES ratings(rating_id) ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id BIGINT NOT NULL,
    genre_id INTEGER NOT NULL,
    CONSTRAINT pk_film_genre PRIMARY KEY(film_id, genre_id),
    CONSTRAINT fk_film_genre_film_id FOREIGN KEY(film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    CONSTRAINT fk_film_genre_genre_id FOREIGN KEY(genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT NOT NULL,
    email VARCHAR(30) NOT NULL,
    login VARCHAR(30) NOT NULL,
    name VARCHAR(30) NOT NULL,
    birthday DATE NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY(user_id),
    CONSTRAINT uk_users_email UNIQUE(email)
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    status VARCHAR(15) NOT NULL DEFAULT 'UNKNOWN',
    CONSTRAINT pk_friends PRIMARY KEY(user_id, friend_id),
    CONSTRAINT fk_friends_user_id FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_friends_friend_id FOREIGN KEY(friend_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY(film_id, user_id),
    CONSTRAINT fk_likes_film_id FOREIGN KEY(film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    CONSTRAINT fk_likes_user_id FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);