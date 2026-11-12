package com.app.book_copy;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Экземпляр книги")
public record BookCopyDto(
        @Schema(description = "ИД экземпляра (copyId)", example = "1")
        Long id,

        @Schema(description = "Инвентарный номер", example = "INV-0001-001")
        String inventoryNumber,

        @Schema(description = "Статус: AVAILABLE, RESERVED, ISSUED, RESTORATION, WRITTEN_OFF", example = "AVAILABLE")
        String status,
        @Schema(description = "Статус по-русски", example = "Свободна")
        String statusName,

        String dateAdded,

        @Schema(description = "ИД книги", example = "1")
        Long bookId,
        String bookName,

        @Schema(description = "Сколько раз выдавался", example = "0")
        int orderingsSize
) {
}
