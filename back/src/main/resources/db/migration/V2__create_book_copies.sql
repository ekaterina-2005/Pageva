-- V2 добавляет экземпляры книг: каждая физическая книга становится отдельной строкой со своим статусом.
-- Создается новая таблица book_copy со ссылкой на book (FOREIGN KEY) и NOT NULL на обязательных полях.

CREATE SEQUENCE book_copy_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE book_copy (
    id               BIGINT      NOT NULL,
    inventory_number VARCHAR(50),
    status           VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    date_added       DATE        NOT NULL DEFAULT CURRENT_DATE,
    book_id          BIGINT      NOT NULL,
    CONSTRAINT book_copy_pkey PRIMARY KEY (id),
    CONSTRAINT fk_book_copy_book FOREIGN KEY (book_id) REFERENCES book (id)
);

-- Перенос данных: число book.quantity превращается в строки-экземпляры с инвентарными номерами INV-0001-001.
-- Экземпляров создается не меньше, чем активных выдач, чтобы в V4 каждой выдаче хватило своего экземпляра.
-- Электронные книги экземпляров не имеют.
INSERT INTO book_copy (id, inventory_number, status, book_id)
SELECT nextval('book_copy_seq'),
       'INV-' || lpad(b.id::text, 4, '0') || '-' || lpad(n::text, 3, '0'),
       'AVAILABLE',
       b.id
FROM book b
CROSS JOIN LATERAL generate_series(
    1,
    GREATEST(b.quantity,
             (SELECT count(*)::int FROM book_ordering o
              WHERE o.book_id = b.id AND o.status IN ('BOOKED', 'DONE')))
) AS n
WHERE b.type = 'ORDINARY'
ORDER BY b.id, n;
