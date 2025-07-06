package ru.yandex.practicum.plus_smart_home_tech.collector.exception;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException e) {
        return ErrorResponse.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(400)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ErrorResponse.builder()
                .error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage())
                .timestamp(LocalDateTime.now())
                .status(400)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return ErrorResponse.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(400)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        return ErrorResponse.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(500)
                .build();
    }
}