package org.weare4saken.trackingsystem.rest.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;


@Slf4j
@ControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(Exception exception, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false)
        );

        return ResponseEntity.badRequest().body(errorMessage);
    }
}