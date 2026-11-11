package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FineStatus {
    UNPAID("Не оплачен"),
    PAID("Оплачен"),
    CANCELLED("Аннулирован"),
    ;

    private final String name;
}
