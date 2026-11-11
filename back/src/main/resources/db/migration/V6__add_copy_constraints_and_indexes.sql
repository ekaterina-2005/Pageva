-- V6 закрепляет правила на уровне БД, данные к этому моменту уже перенесены.
-- Инвентарный номер обязателен и уникален, статус экземпляра проверяется через CHECK.

ALTER TABLE book_copy ALTER COLUMN inventory_number SET NOT NULL;

ALTER TABLE book_copy
    ADD CONSTRAINT uq_book_copy_inventory_number UNIQUE (inventory_number);

ALTER TABLE book_copy
    ADD CONSTRAINT chk_book_copy_status
    CHECK (status IN ('AVAILABLE', 'RESERVED', 'ISSUED', 'RESTORATION', 'WRITTEN_OFF'));

-- Частичный уникальный индекс: один экземпляр не может быть сразу в двух активных выдачах.
CREATE UNIQUE INDEX ux_book_ordering_active_copy
    ON book_ordering (book_copy_id)
    WHERE status IN ('BOOKED', 'DONE');

-- Индексы для частых запросов: выдачи по статусу, свободные экземпляры книги, неоплаченные штрафы.
CREATE INDEX ix_book_ordering_status ON book_ordering (status);
CREATE INDEX ix_book_copy_book_status ON book_copy (book_id, status);
CREATE INDEX ix_fine_status ON fine (status);
