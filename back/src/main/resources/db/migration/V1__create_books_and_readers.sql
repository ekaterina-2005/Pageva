-- V1 создает исходную структуру БД в том виде, в каком она была до появления экземпляров и штрафов.
-- Читатели, справочники, книги, отзывы, выдачи по книге и подписки.
-- Выдача пока ссылается на книгу целиком, а количество книг хранится числом в book.quantity.

CREATE SEQUENCE app_user_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE category_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE discount_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE subscription_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE book_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE book_comment_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE book_ordering_seq START WITH 1 INCREMENT BY 1;

-- Читатели и сотрудники. Роль ограничена через CHECK.
CREATE TABLE app_user (
    id                 BIGINT        NOT NULL,
    username           VARCHAR(255),
    password           VARCHAR(255),
    role               VARCHAR(255),
    fio                VARCHAR(255),
    email              VARCHAR(255),
    date               VARCHAR(255),
    img                VARCHAR(1000),
    account_non_locked BOOLEAN       NOT NULL,
    CONSTRAINT app_user_pkey PRIMARY KEY (id),
    CONSTRAINT app_user_role_check CHECK (role IN ('ADMIN', 'MANAGER', 'USER'))
);

CREATE TABLE category (
    id   BIGINT       NOT NULL,
    name VARCHAR(255),
    CONSTRAINT category_pkey PRIMARY KEY (id)
);

CREATE TABLE discount (
    id      BIGINT       NOT NULL,
    name    VARCHAR(255),
    percent INTEGER      NOT NULL,
    CONSTRAINT discount_pkey PRIMARY KEY (id)
);

CREATE TABLE subscription (
    id       BIGINT       NOT NULL,
    name     VARCHAR(255),
    duration INTEGER      NOT NULL,
    price    REAL         NOT NULL,
    CONSTRAINT subscription_pkey PRIMARY KEY (id)
);

-- Книги. quantity хранит количество экземпляров простым числом.
CREATE TABLE book (
    id          BIGINT        NOT NULL,
    name        VARCHAR(255),
    author      VARCHAR(255),
    page        INTEGER       NOT NULL,
    date        VARCHAR(255),
    read        VARCHAR(255),
    price       REAL          NOT NULL,
    quantity    INTEGER       NOT NULL,
    repair      BOOLEAN       NOT NULL,
    visibility  BOOLEAN       NOT NULL,
    description VARCHAR(5000),
    img         VARCHAR(1000),
    file        VARCHAR(1000),
    age         VARCHAR(255),
    type        VARCHAR(255),
    category_id BIGINT,
    CONSTRAINT book_pkey PRIMARY KEY (id),
    CONSTRAINT book_age_check CHECK (age IN ('A0_10', 'A11_15', 'A16_20', 'A21')),
    CONSTRAINT book_type_check CHECK (type IN ('ORDINARY', 'ELECTRONIC')),
    CONSTRAINT fk_book_category FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE book_comment (
    id       BIGINT        NOT NULL,
    date     VARCHAR(255),
    score    INTEGER       NOT NULL,
    text     VARCHAR(5000),
    book_id  BIGINT,
    owner_id BIGINT,
    CONSTRAINT book_comment_pkey PRIMARY KEY (id),
    CONSTRAINT fk_book_comment_book FOREIGN KEY (book_id) REFERENCES book (id),
    CONSTRAINT fk_book_comment_owner FOREIGN KEY (owner_id) REFERENCES app_user (id)
);

-- Выдачи. Ссылаются на книгу, а не на конкретный экземпляр.
CREATE TABLE book_ordering (
    id               BIGINT       NOT NULL,
    date             VARCHAR(255),
    days             INTEGER      NOT NULL,
    date_start       VARCHAR(255),
    date_end         VARCHAR(255),
    ordering_percent INTEGER,
    price            REAL         NOT NULL,
    status           VARCHAR(255),
    book_id          BIGINT,
    discount_id      BIGINT,
    owner_id         BIGINT,
    CONSTRAINT book_ordering_pkey PRIMARY KEY (id),
    CONSTRAINT book_ordering_status_check CHECK (status IN ('WAITING', 'REJECTED', 'BOOKED', 'DONE', 'RETURNED')),
    CONSTRAINT fk_book_ordering_book FOREIGN KEY (book_id) REFERENCES book (id),
    CONSTRAINT fk_book_ordering_discount FOREIGN KEY (discount_id) REFERENCES discount (id),
    CONSTRAINT fk_book_ordering_owner FOREIGN KEY (owner_id) REFERENCES app_user (id)
);

CREATE TABLE subscription_ordering (
    id       BIGINT       NOT NULL,
    name     VARCHAR(255),
    duration INTEGER      NOT NULL,
    price    REAL         NOT NULL,
    card     VARCHAR(255),
    cvv      VARCHAR(255),
    date     VARCHAR(255),
    owner_id BIGINT,
    CONSTRAINT subscription_ordering_pkey PRIMARY KEY (id),
    CONSTRAINT fk_subscription_ordering_owner FOREIGN KEY (owner_id) REFERENCES app_user (id)
);

-- Тестовые данные, которые лежат в базе до миграций V2-V6.
-- По одной записи на каждый случай, который потом обрабатывают миграции. Пароль у всех Library1!

INSERT INTO app_user (id, username, password, role, fio, email, date, img, account_non_locked) VALUES
(1, 'admin',   '$2a$12$pBQjg0fZ.lvq7SaGQc/f8eCk4gJWj0zu1Yfpxl0cyQIlMmFc5dTx2', 'ADMIN',   'Иванов Пётр Сергеевич',     'admin@library.by',   '2026-01-10', '/img/avatar.png', true),
(2, 'manager', '$2a$12$pBQjg0fZ.lvq7SaGQc/f8eCk4gJWj0zu1Yfpxl0cyQIlMmFc5dTx2', 'MANAGER', 'Петрова Анна Николаевна',   'manager@library.by', '2026-01-15', '/img/avatar.png', true),
-- reader1: активные выдачи, вернул книгу вовремя
(3, 'reader1', '$2a$12$pBQjg0fZ.lvq7SaGQc/f8eCk4gJWj0zu1Yfpxl0cyQIlMmFc5dTx2', 'USER',    'Смирнова Мария Андреевна',  'reader1@library.by', '2026-02-01', '/img/avatar.png', true),
-- reader2: вернул книгу с просрочкой, почта записана с заглавными буквами
(4, 'reader2', '$2a$12$pBQjg0fZ.lvq7SaGQc/f8eCk4gJWj0zu1Yfpxl0cyQIlMmFc5dTx2', 'USER',    'Козлов Игорь Викторович',   'Reader2@Library.BY', '2026-02-05', '/img/avatar.png', true),
-- reader3: заблокирован, выдач нет
(5, 'reader3', '$2a$12$pBQjg0fZ.lvq7SaGQc/f8eCk4gJWj0zu1Yfpxl0cyQIlMmFc5dTx2', 'USER',    'Новикова Ольга Павловна',   'reader3@library.by', '2026-02-10', '/img/avatar.png', false);

-- Поэзия остается пустой категорией
INSERT INTO category (id, name) VALUES
(1, 'Художественная литература'),
(2, 'Фантастика'),
(3, 'Поэзия');

INSERT INTO discount (id, name, percent) VALUES
(1, 'Новый читатель', 10);

-- subscription и subscription_ordering используют одну последовательность subscription_seq
INSERT INTO subscription (id, name, duration, price) VALUES
(1, 'Месячная', 30, 9.90);

-- book 1: обычная, 3 экземпляра, 2 активные выдачи
-- book 2: обычная, в quantity записан 1, а активных выдач 2 (данные расходятся, V2 это исправит)
-- book 3: электронная, экземпляров нет
-- book 4: обычная, скрыта из каталога, только завершенные выдачи
-- book 5: обычная, quantity = 0, на реставрации
INSERT INTO book (id, name, author, page, date, read, price, quantity, repair, visibility, description, img, file, age, type, category_id) VALUES
(1, 'Война и мир',              'Лев Толстой',       1225, '1869', '23:59', 18.50, 3, false, true,  'Роман-эпопея о русском обществе в эпоху войн против Наполеона.', '', '',                   'A16_20', 'ORDINARY',   1),
(2, 'Преступление и наказание', 'Фёдор Достоевский',  671, '1866', '20:00', 14.90, 1, false, true,  'Психологический роман о нравственном выборе.',                   '', '',                   'A16_20', 'ORDINARY',   1),
(3, 'Мастер и Маргарита',       'Михаил Булгаков',    480, '1967', '14:30',  0.00, 0, false, true,  'Мистический роман о визите дьявола в Москву.',                   '', '/files/books/3.pdf', 'A21',    'ELECTRONIC', 2),
(4, 'Евгений Онегин',           'Александр Пушкин',   224, '1833', '18:00', 12.40, 2, false, false, 'Роман в стихах о судьбе молодого дворянина.',                    '', '',                   'A11_15', 'ORDINARY',   1),
(5, 'Пикник на обочине',        'Стругацкие',         256, '1972', '12:00',  9.90, 0, true,  true,  'Повесть о Зоне и сталкерах.',                                    '', '',                   'A16_20', 'ORDINARY',   2);

INSERT INTO book_comment (id, date, score, text, book_id, owner_id) VALUES
(1, '2026-03-01 12:00:00', 5, 'Перечитываю уже третий раз.', 1, 3),
(2, '2026-03-05 18:30:00', 4, 'Удобно читать в электронном виде.', 3, 4);

-- Выдачи во всех статусах
-- 1 BOOKED, 2 DONE со скидкой, 3 DONE с текущей просрочкой, 4 BOOKED второй экземпляр книги 2,
-- 5 DONE электронной книги, 6 RETURNED вовремя, 7 RETURNED с просрочкой 12 дней, 8 WAITING, 9 REJECTED
INSERT INTO book_ordering (id, date, days, date_start, date_end, ordering_percent, price, status, book_id, discount_id, owner_id) VALUES
(1, '2026-10-15', 14, '2026-10-12', '',           0,  18.50, 'BOOKED',   1, NULL, 3),
(2, '2026-10-01', 30, '2026-09-28', '',           10, 18.50, 'DONE',     1, 1,    3),
(3, '2026-09-20', 14, '2026-09-18', '',           0,  14.90, 'DONE',     2, NULL, 4),
(4, '2026-10-16', 7,  '2026-10-14', '',           0,  14.90, 'BOOKED',   2, NULL, 3),
(5, '2026-10-05', 10, '2026-10-05', '',           0,  0.00,  'DONE',     3, NULL, 4),
(6, '2026-09-01', 7,  '2026-08-30', '2026-09-06', 0,  12.40, 'RETURNED', 4, NULL, 3),
(7, '2026-08-01', 7,  '2026-07-30', '2026-08-20', 0,  12.40, 'RETURNED', 4, NULL, 4),
(8, '2026-10-25', 14, '2026-10-18', '',           0,  12.40, 'WAITING',  4, NULL, 3),
(9, '2026-10-10', 7,  '2026-10-08', '',           0,  18.50, 'REJECTED', 1, NULL, 4);

INSERT INTO subscription_ordering (id, name, duration, price, card, cvv, date, owner_id) VALUES
(2, 'Месячная', 30, 9.90, '4111111111111111', '123', '2026-10-01', 3);

-- Сдвигаем последовательности, чтобы приложение продолжило нумерацию после тестовых записей
SELECT setval('app_user_seq', 5);
SELECT setval('category_seq', 3);
SELECT setval('discount_seq', 1);
SELECT setval('subscription_seq', 2);
SELECT setval('book_seq', 5);
SELECT setval('book_comment_seq', 2);
SELECT setval('book_ordering_seq', 9);
