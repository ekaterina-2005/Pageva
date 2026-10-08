package com.app.subscription;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SubscriptionDto(
        Long id,

        @Size(min = 1, max = 255, message = "name is required length 1-255")
        @NotEmpty(message = "name is required")
        String name,
        @Min(value = 1, message = "duration is required min 1")
        @Max(value = 1000, message = "duration is required max 1000")
        int duration,
        @Min(value = 1, message = "price is required min 1")
        @Max(value = 1000000, message = "price is required max 1000000")
        float price
) {
}
