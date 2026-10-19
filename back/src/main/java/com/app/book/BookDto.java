package com.app.book;

import com.app.book_comment.BookCommentDto;
import com.app.book_ordering.BookOrderingDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BookDto(
        Long id,

        @Size(min = 1, max = 255, message = "name is required length 1-255")
        @NotEmpty(message = "name is required")
        String name,
        @Size(min = 1, max = 255, message = "author is required length 1-255")
        @NotEmpty(message = "author is required")
        String author,
        @Min(value = 1, message = "page is required min 1")
        @Max(value = 1000000, message = "page is required max 1000000")
        int page,
        @Size(min = 1, max = 255, message = "date is required length 1-255")
        @NotEmpty(message = "date is required")
        String date,
        @Size(min = 1, max = 255, message = "read is required length 1-255")
        @NotEmpty(message = "read is required")
        String read,
        @Min(value = 0, message = "price is required min 0.01")
        @Max(value = 1000000, message = "price is required max 1000000")
        float price,
        @Min(value = 0, message = "quantity is required min 0")
        @Max(value = 1000000, message = "quantity is required max 1000000")
        int quantity,

        int quantityFree,

        boolean repair,
        boolean visibility,

        @Size(min = 1, max = 5000, message = "description is required length 1-255")
        @NotEmpty(message = "description is required")
        String description,

        String img,
        String file,

        String age,
        String ageName,

        String type,
        String typeName,

        Long categoryId,
        String categoryName,

        int count,

        float score,

        List<BookCommentDto> comments,
        List<BookOrderingDto> orderings
) {
}
