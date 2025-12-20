CREATE DATABASE filestorage;
USE filestorage;

CREATE TABLE IF NOT EXISTS users (
    id bigserial primary key,
    name varchar(64) not null unique,
    email varchar(64) not null unique,
    bucket_name varchar(63) not null unique,
    password varchar(80) not null
);

CREATE TABLE IF NOT EXISTS roles (
    id serial primary key,
    name varchar(64) not null unique
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id bigint not null references users(id),
    role_id int not null references roles(id),
    primary key (user_id, role_id),
    unique (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS tags (
    id bigserial primary key,
    name varchar(64) not null unique
);

CREATE TABLE IF NOT EXISTS files (
    id bigserial primary key,
    name varchar(64) not null unique,
    description varchar(64),
    upload_date varchar(64) not null,
    number_of_downloads int not null,
    extension varchar(64) not null,
    access_modifier varchar(64) not null,
    storage_file_name varchar(64) not null unique,
    user_id bigserial not null,
    foreign key (user_id) references users(id)
);

CREATE TABLE IF NOT EXISTS tag_files (
    tag_id bigint not null references tags(id),
    file_id int not null references files(id),
    primary key (tag_id, file_id),
    unique (tag_id, file_id)
);

INSERT INTO roles (name) values ('ROLE_USER');
