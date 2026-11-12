package com.app.appUser;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Читатель / пользователь")
public record UserDto(
        @Schema(description = "ИД пользователя (readerId)", example = "2")
        Long id,
        @Schema(description = "Логин", example = "reader1")
        String username,
        @Schema(description = "Роль: ADMIN, MANAGER, USER", example = "USER")
        String role,

        @Schema(description = "ФИО", example = "Иванова Анна")
        String fio,
        String date,
        @Schema(description = "Почта", example = "reader1@mail.ru")
        String email,

        String img,

        @Schema(description = "Заблокирован", example = "false")
        boolean lock
) {
}
