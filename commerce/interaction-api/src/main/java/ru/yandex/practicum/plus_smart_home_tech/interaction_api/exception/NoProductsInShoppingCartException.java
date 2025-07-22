package ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception;

public class NoProductsInShoppingCartException extends RuntimeException {
    public NoProductsInShoppingCartException(String message) {
        super(message);
    }
}
