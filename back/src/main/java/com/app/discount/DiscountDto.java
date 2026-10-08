package com.app.discount;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record DiscountDto(
        Long id,

        @Size(min = 1, max = 255, message = "name is required length 1-255")
        @NotEmpty(message = "name is required")
        String name,
        @Min(value = 1, message = "percent is required min 1")
        @Max(value = 100, message = "percent is required max 100")
        int percent
) {
}
