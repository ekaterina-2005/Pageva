package com.app.subscription_ordering;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SubscriptionOrderingDto(
        Long id,

        @Size(min = 1, max = 255, message = "card is required length 1-255")
        @NotEmpty(message = "card is required")
        String card,
        @Size(min = 1, max = 255, message = "cvv is required length 1-255")
        @NotEmpty(message = "cvv is required")
        String cvv,

        String name,
        int duration,
        float price,

        String date,
        boolean active

) {
}
