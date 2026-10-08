package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookType {
    ORDINARY("Обычная"),
    ELECTRONIC("Электронная"),
    ;

    private final String name;
}

