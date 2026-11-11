-- V3 меняет существующую таблицу выдач через ALTER TABLE: выдача теперь может ссылаться на конкретный экземпляр.
-- Колонка допускает NULL, потому что у заявок WAITING и REJECTED и у электронных книг экземпляра нет.
-- Связь с book_id остается, чтобы не потерять историю и заявки без экземпляра.

ALTER TABLE book_ordering ADD COLUMN book_copy_id BIGINT;

ALTER TABLE book_ordering
    ADD CONSTRAINT fk_book_ordering_book_copy
    FOREIGN KEY (book_copy_id) REFERENCES book_copy (id);
