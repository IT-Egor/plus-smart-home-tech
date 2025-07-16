package ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
