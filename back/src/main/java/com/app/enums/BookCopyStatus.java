package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookCopyStatus {
    AVAILABLE("Свободна"),
    RESERVED("Забронирована"),
    ISSUED("Выдана"),
    RESTORATION("На реставрации"),
    WRITTEN_OFF("Списана"),
    ;

    private final String name;
}
