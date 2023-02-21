DELETE FROM user_meal;
DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO user_meal (user_id, date_time, description, calories)
VALUES (100001,'2022-08-1 14:01:00','Админ ланч', 510),
       (100001,'2022-08-1 14:02:00','Админ хуянч', 510),
       (100001,'2022-08-1 14:00:00','Админ ужин', 510),
       (100000,'2022-08-1 14:02:00','Юзер хуянч', 510),
       (100000,'2022-08-1 14:01:00','Юзер ланч', 510);

