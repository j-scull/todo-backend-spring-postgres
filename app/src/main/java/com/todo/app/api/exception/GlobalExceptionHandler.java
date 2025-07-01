package com.todo.app.api.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.app.api.exception.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final ObjectMapper mapper;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(exception = MethodArgumentNotValidException.class, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.warn("Bad request: {}", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.message("Bad Request")
			.errorDetails(e.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
				.collect(Collectors.toList()))
			.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(exception = HttpMessageNotReadableException.class, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.warn("Bad request: {}", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.message("Bad Request")
			.errorDetails(Collections.singletonList(e.getLocalizedMessage()))
			.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(exception = MethodArgumentTypeMismatchException.class,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException e) {
		log.warn("Bad request: {}", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.message("Bad Request")
			.errorDetails(Collections.singletonList(e.getLocalizedMessage()))
			.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(exception = NoResourceFoundException.class, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
		log.warn("Not found: {}", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.message(e.getLocalizedMessage())
			.errorDetails(List.of())
			.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(exception = HttpRequestMethodNotSupportedException.class,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException e) {
		log.warn("Method not allowed: {}", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder()
			.statusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
			.message(e.getLocalizedMessage())
			.errorDetails(List.of())
			.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler(exception = HttpMediaTypeNotAcceptableException.class)
	public ResponseEntity<String> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e)
			throws JsonProcessingException {
		log.warn("Media type not acceptable");
		ErrorResponse errorResponse = ErrorResponse.builder()
			.statusCode(HttpStatus.NOT_ACCEPTABLE.value())
			.message("acceptable MIME type: " + MediaType.APPLICATION_JSON_VALUE)
			.errorDetails(List.of())
			.build();
		return new ResponseEntity<>(mapper.writeValueAsString(errorResponse), HttpStatus.NOT_ACCEPTABLE);
	}

	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler(exception = HttpMediaTypeNotSupportedException.class, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
			HttpMediaTypeNotSupportedException e) {
		log.warn("Unsupported media type: {}", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder()
			.statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
			.message(e.getLocalizedMessage())
			.errorDetails(List.of())
			.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.warn("Exception: {}", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
			.message("error occurred")
			.errorDetails(List.of())
			.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
