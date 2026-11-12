package com.app.book_ordering;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Заявка на выдачу")
public record BookOrderingDto(
        @Schema(description = "ИД заявки", example = "5")
        Long id,

        @Size(min = 1, max = 255, message = "date is required length 1-255")
        @NotEmpty(message = "date is required")
        @Schema(description = "Дата начала аренды (ГГГГ-ММ-ДД)", example = "2026-10-01")
        String date,
        @Min(value = 1, message = "days is required min 1")
        @Max(value = 100, message = "days is required max 100")
        @Schema(description = "Срок аренды, дней", example = "3")
        int days,

        String dateStart,
        @Schema(description = "Фактическая дата возврата", example = "2026-11-12")
        String dateEnd,

        int percent,
        float price,
        float sum,

        @Schema(description = "Статус: WAITING, BOOKED, DONE, RETURNED, REJECTED", example = "DONE")
        String status,
        String statusName,

        Long bookId,
        String bookName,
        String bookImg,

        Long discountId,
        String discountName,

        @Schema(description = "ИД читателя", example = "2")
        Long ownerId,
        @Schema(description = "ФИО читателя", example = "Иванова Анна")
        String  ownerFio,

        @Schema(description = "ИД выданного экземпляра", example = "1")
        Long bookCopyId,
        @Schema(description = "Инвентарный номер экземпляра", example = "INV-0001-001")
        String inventoryNumber,

        @Schema(description = "Дней просрочки (0 — без просрочки)", example = "7")
        int overdueDays,
        Long fineId,
        @Schema(description = "Сумма штрафа за просрочку, BYN", example = "3.50")
        BigDecimal fineAmount,
        @Schema(description = "Статус штрафа: UNPAID, PAID, CANCELLED", example = "UNPAID")
        String fineStatus,
        String fineStatusName
) {
}
