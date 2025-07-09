package ru.yandex.practicum.plus_smart_home_tech.analyzer.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
