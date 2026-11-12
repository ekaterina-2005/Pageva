package com.app.fine;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Штраф за просроченный возврат")
public record FineDto(
        @Schema(description = "ИД штрафа", example = "1")
        Long id,

        @Schema(description = "Дней просрочки", example = "39")
        int daysOverdue,
        @Schema(description = "Сумма, BYN", example = "581.10")
        BigDecimal amount,

        @Schema(description = "Статус: UNPAID, PAID, CANCELLED", example = "UNPAID")
        String status,
        String statusName,

        String dateCreated,
        String datePaid,

        @Schema(description = "ИД выдачи", example = "3")
        Long orderingId,

        Long bookId,
        String bookName,
        String inventoryNumber,

        @Schema(description = "ИД читателя", example = "4")
        Long ownerId,
        String ownerFio
) {
}
