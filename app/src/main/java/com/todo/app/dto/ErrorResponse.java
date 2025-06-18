package com.todo.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
@ToString
public class ErrorResponse {

	private int statusCode;

	private String message;

	private List<String> errorDetails;

}
