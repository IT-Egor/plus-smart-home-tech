package ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
