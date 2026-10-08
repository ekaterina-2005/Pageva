package com.app.book_ordering;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record BookOrderingDto(
        Long id,

        @Size(min = 1, max = 255, message = "date is required length 1-255")
        @NotEmpty(message = "date is required")
        String date,
        @Min(value = 1, message = "days is required min 1")
        @Max(value = 100, message = "days is required max 100")
        int days,

        String dateStart,
        String dateEnd,

        int percent,
        float price,
        float sum,

        String status,
        String statusName,

        Long bookId,
        String bookName,
        String bookImg,

        Long discountId,
        String discountName,

        Long ownerId,
        String  ownerFio
) {
}
