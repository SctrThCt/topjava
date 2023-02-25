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
VALUES (100001,'2022-01-8 8:00:00','Админ завтрак', 510),
       (100001,'2022-01-8 13:02:00','Админ обед', 700),
       (100001,'2022-01-8 18:01:00','Админ ужин', 510),
       (100000,'2022-01-8 13:02:00','Юзер обед', 1200),
       (100000,'2022-01-8 18:01:00','Юзер ужин', 600);

