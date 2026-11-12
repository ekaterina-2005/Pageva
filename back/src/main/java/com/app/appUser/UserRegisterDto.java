package com.app.appUser;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

// Данные регистрации; роль из запроса не принимается (всегда USER, кроме самого первого пользователя)
@Schema(description = "Регистрация читателя")
public record UserRegisterDto(
        @Schema(description = "Логин", example = "reader4")
        @Size(min = 1, max = 255, message = "username is required length 1-255")
        @NotEmpty(message = "username is required")
        String username,

        @Schema(description = "Пароль", example = "Library1!")
        @Size(min = 1, max = 255, message = "password is required length 1-255")
        @NotEmpty(message = "password is required")
        String password,

        @Schema(description = "ФИО", example = "Соколова Елена Игоревна")
        @Size(min = 1, max = 255, message = "fio is required length 1-255")
        @NotEmpty(message = "fio is required")
        String fio,

        @Schema(description = "Почта", example = "reader4@library.by")
        @Size(min = 1, max = 255, message = "email is required length 1-255")
        @NotEmpty(message = "email is required")
        @Email(message = "email is required")
        String email
) {
    public AppUser toUser() {
        AppUser user = new AppUser(username);
        user.setPassword(password);
        user.setFio(fio);
        user.setEmail(email);
        return user;
    }
}
