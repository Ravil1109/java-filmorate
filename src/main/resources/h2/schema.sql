-- Справочник жанров
create table if not exists genres (
    id bigint auto_increment primary key,
    name varchar2(255) not null
);

-- Справочник рейтингов
create table if not exists mpa (
    id bigint auto_increment primary key,
	name varchar2(5)
);

-- Фильмы
create table if not exists films (
    id bigint auto_increment primary key,
    name varchar2(255) not null,
    description varchar2(1000),
    release_date date,
    duration integer,
    mpa_id bigint,
    foreign key (mpa_id) references mpa(id)
);


-- Жанры фильма
create table if not exists film_genres (
    film_id bigint not null,
    genre_id bigint not null,
    foreign key (genre_id) references genres(id) on delete cascade,
    foreign key (film_id) references films(id) on delete cascade,
	primary key (film_id, genre_id)
);

-- Пользователи
create table if not exists users (
    id bigint auto_increment primary key,
    login varchar2(255) not null unique,
    name varchar2(255),
    email varchar2(255) not null unique,
    birthday date
);

-- Лайки фильмов
create table if not exists film_likes (
    film_id bigint,
    user_id bigint,
    primary key (film_id, user_id),
    foreign key (film_id) references films(id) on delete cascade,
    foreign key (user_id) references users(id) on delete cascade
);

-- Друзья пользователей
create table if not exists friends (
    user_id bigint,
    friend_id bigint,
    primary key (user_id, friend_id),
    foreign key (user_id) references users(id) on delete cascade,
    foreign key (friend_id) references users(id) on delete cascade
);