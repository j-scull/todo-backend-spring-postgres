package com.todo.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
public class ErrorResponse {

    private HttpStatus status;
    private String message;
    private List<String> errors;
}
