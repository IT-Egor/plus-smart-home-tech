package ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception;

public class NotEnoughProductsInWarehouseException extends RuntimeException {
    public NotEnoughProductsInWarehouseException(String message) {
        super(message);
    }
}
