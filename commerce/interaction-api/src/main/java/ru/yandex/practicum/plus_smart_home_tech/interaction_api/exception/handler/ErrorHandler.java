package ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.handler;

import feign.FeignException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.*;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException e) {
        String reasonMessage = "Feign error";
        log.error("FEIGN_ERROR: {}", reasonMessage, e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(e.status())
                .build();
        return ResponseEntity.status(e.status()).body(errorResponse);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException e) {
        String reasonMessage = "Validation failed";
        log.error("BAD_REQUEST: {}", reasonMessage, e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String reasonMessage = "Method argument not valid";
        log.error("BAD_REQUEST: {}", reasonMessage, e);
        return ErrorResponse.builder()
                .message(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage())
                .reason(reasonMessage)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        String reasonMessage = "Handler method not valid";
        log.error("BAD_REQUEST: {}", reasonMessage, e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoProductsInShoppingCartException(NoProductsInShoppingCartException e) {
        String reasonMessage = "Shopping cart is empty";
        log.error("BAD_REQUEST: {}", reasonMessage, e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        String reasonMessage = "Entity not found";
        log.error("NOT_FOUND: {}", reasonMessage, e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException e) {
        String reasonMessage = "User is not authorized";
        log.error("UNAUTHORIZED: {}", reasonMessage, e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAlreadyExistsException(AlreadyExistsException e) {
        String reasonMessage = "Object already exists";
        log.error("ALREADY EXISTS: {}", reasonMessage, e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotEnoughProductsInWarehouseException(NotEnoughProductsInWarehouseException e) {
        String reasonMessage = "Not enough products in warehouse";
        log.error("NOT ENOUGH PRODUCTS: {}", reasonMessage, e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(RuntimeException e) {
        String reasonMessage = "Unknown error";
        log.error("INTERNAL_SERVER_ERROR: {}", reasonMessage, e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }
}
