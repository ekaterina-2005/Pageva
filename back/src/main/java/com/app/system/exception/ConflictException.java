package com.app.system.exception;

// Конфликт состояния (экземпляр уже выдан, выдача уже возвращена, неоплаченный штраф) -> 409
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
