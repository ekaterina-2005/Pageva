package com.app.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CategoryDto(
        Long id,

        @Size(min = 1, max = 255, message = "name is required length 1-255")
        @NotEmpty(message = "name is required")
        String name
) {
}
