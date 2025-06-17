package com.todo.app.api.exception;

import com.todo.app.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // todo

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(exception = BadRequest.class, produces = "application/json")
    public ResponseEntity<String> handleBadRequest(BadRequest e) {
        log.warn("Bad request: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(exception = NoResourceFoundException.class, produces = "application/json")
    public ResponseEntity<ErrorResponse> handleNotFound(NoResourceFoundException e) {
        log.warn("Not found: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(e.getLocalizedMessage())
                .errors(List.of("error occurred"))
                .build();
        return new ResponseEntity<>(
                errorResponse, errorResponse.getStatus());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(exception = HttpRequestMethodNotSupportedException.class, produces = "application/json")
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("Method not allowed: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    //HttpMediaTypeNotSupportedException
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(exception = HttpMediaTypeNotSupportedException.class, produces = "application/json")
    public ResponseEntity<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.warn("Unsupported media type: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.warn("Exception: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getLocalizedMessage())
                .errors(List.of("error occurred"))
                .build();
        return new ResponseEntity<>(
                errorResponse, errorResponse.getStatus());
    }

}
