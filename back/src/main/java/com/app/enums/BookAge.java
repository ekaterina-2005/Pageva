package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookAge {
    A0_10("0-10"),
    A11_15("11-15"),
    A16_20("16-20"),
    A21("21+"),
    ;

    private final String name;
}

