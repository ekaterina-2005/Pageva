package com.app.appUser;

public record UserDto(
        Long id,
        String username,
        String role,

        String fio,
        String date,
        String email,

        String img,

        boolean lock
) {
}
