package com.app.fine;

import java.math.BigDecimal;

public record FineDto(
        Long id,

        int daysOverdue,
        BigDecimal amount,

        String status,
        String statusName,

        String dateCreated,
        String datePaid,

        Long orderingId,

        Long bookId,
        String bookName,
        String inventoryNumber,

        Long ownerId,
        String ownerFio
) {
}
