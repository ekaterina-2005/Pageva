package com.app.book_copy;

public record BookCopyDto(
        Long id,

        String inventoryNumber,

        String status,
        String statusName,

        String dateAdded,

        Long bookId,
        String bookName,

        int orderingsSize
) {
}
