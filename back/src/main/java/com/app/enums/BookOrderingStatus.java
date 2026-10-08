package com.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookOrderingStatus {
    WAITING("Ожидание"),
    REJECTED("Отменено"),
    BOOKED("Забронировано"),
    DONE("У клиента"),
    RETURNED("Возвращено"),
    ;

    private final String name;
}

