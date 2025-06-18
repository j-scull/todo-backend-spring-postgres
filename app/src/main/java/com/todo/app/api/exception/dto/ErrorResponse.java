package com.todo.app.api.exception.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ErrorResponse {

	private int statusCode;

	private String message;

	private List<String> errorDetails;

}
