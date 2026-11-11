-- V4 переносит существующие выдачи в новую структуру через UPDATE, ни одна запись не удаляется.

-- Активным выдачам (BOOKED и DONE) обычных книг назначаются разные экземпляры той же книги по порядку.
WITH active AS (
    SELECT o.id, o.book_id,
           row_number() OVER (PARTITION BY o.book_id ORDER BY o.id) AS rn
    FROM book_ordering o
    JOIN book b ON b.id = o.book_id
    WHERE o.status IN ('BOOKED', 'DONE') AND b.type = 'ORDINARY'
), copies AS (
    SELECT c.id, c.book_id,
           row_number() OVER (PARTITION BY c.book_id ORDER BY c.id) AS rn
    FROM book_copy c
)
UPDATE book_ordering o
SET book_copy_id = c.id
FROM active a
JOIN copies c ON c.book_id = a.book_id AND c.rn = a.rn
WHERE o.id = a.id;

-- Завершенным выдачам для истории назначается первый экземпляр книги.
UPDATE book_ordering o
SET book_copy_id = (SELECT min(c.id) FROM book_copy c WHERE c.book_id = o.book_id)
WHERE o.status = 'RETURNED' AND o.book_copy_id IS NULL;

-- Статус экземпляра выставляется по его активной выдаче.
UPDATE book_copy c SET status = 'RESERVED'
FROM book_ordering o
WHERE o.book_copy_id = c.id AND o.status = 'BOOKED';

UPDATE book_copy c SET status = 'ISSUED'
FROM book_ordering o
WHERE o.book_copy_id = c.id AND o.status = 'DONE';

-- Количество книги теперь равно числу ее экземпляров.
UPDATE book b
SET quantity = (SELECT count(*) FROM book_copy c WHERE c.book_id = b.id)
WHERE b.type = 'ORDINARY';

-- Самопроверка: если активная выдача осталась без экземпляра, миграция откатится целиком.
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM book_ordering o
               JOIN book b ON b.id = o.book_id
               WHERE b.type = 'ORDINARY'
                 AND o.status IN ('BOOKED', 'DONE')
                 AND o.book_copy_id IS NULL) THEN
        RAISE EXCEPTION 'V4: active ordering without book copy';
    END IF;
END $$;
