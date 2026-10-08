package com.app.book_comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record BookCommentDto(
        Long id,

        String date,

        @Min(value = 1, message = "score is required min 1")
        @Max(value = 10, message = "score is required max 10")
        int score,

        @Size(min = 1, max = 5000, message = "text is required length 1-255")
        @NotEmpty(message = "text is required")
        String text,

        Long ownerId,
        String ownerFio
) {
}
