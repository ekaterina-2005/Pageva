-- V5 добавляет штрафы за просрочку. Новая таблица fine связана с выдачей через FOREIGN KEY.
-- UNIQUE на ordering_id: за одну выдачу не больше одного штрафа.
-- CHECK не дает записать отрицательную сумму, нулевую просрочку или неизвестный статус.

CREATE SEQUENCE fine_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE fine (
    id           BIGINT        NOT NULL,
    ordering_id  BIGINT        NOT NULL,
    days_overdue INTEGER       NOT NULL,
    amount       NUMERIC(10,2) NOT NULL,
    status       VARCHAR(20)   NOT NULL DEFAULT 'UNPAID',
    date_created DATE          NOT NULL DEFAULT CURRENT_DATE,
    date_paid    DATE,
    CONSTRAINT fine_pkey PRIMARY KEY (id),
    CONSTRAINT fk_fine_ordering FOREIGN KEY (ordering_id) REFERENCES book_ordering (id),
    CONSTRAINT uq_fine_ordering UNIQUE (ordering_id),
    CONSTRAINT chk_fine_amount CHECK (amount >= 0),
    CONSTRAINT chk_fine_days CHECK (days_overdue > 0),
    CONSTRAINT chk_fine_status CHECK (status IN ('UNPAID', 'PAID', 'CANCELLED'))
);

-- Штрафы начисляются по уже возвращенным выдачам, где дата возврата позже срока.
-- Просрочка = (дата возврата - дата начала) - дни аренды. Сумма = дни просрочки * цена в день, минимум 0.50 в день.
INSERT INTO fine (id, ordering_id, days_overdue, amount, status, date_created)
SELECT nextval('fine_seq'),
       o.id,
       x.days_late,
       round((x.days_late * GREATEST(o.price, 0.50))::numeric, 2),
       'UNPAID',
       x.returned
FROM book_ordering o
CROSS JOIN LATERAL (
    SELECT NULLIF(o.date_end, '')::date AS returned,
           (NULLIF(o.date_end, '')::date - o.date::date) - o.days AS days_late
) x
WHERE o.status = 'RETURNED'
  AND x.returned IS NOT NULL
  AND x.days_late > 0
ORDER BY o.id;
