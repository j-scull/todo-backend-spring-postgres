package com.todo.app.api.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.app.api.exception.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

	private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler(new ObjectMapper());

	@Test
	void handleMethodArgumentNotValidException_returnsValidResponse() {
		MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
		BindingResult bindingResult = mock(BindingResult.class);
		List<FieldError> fieldErrors = Arrays.asList(new FieldError("", "title", "must not be null"),
				new FieldError("", "order", "must not be null"));
		when(exception.getMessage()).thenReturn("Invalid request fields");
		when(exception.getBindingResult()).thenReturn(bindingResult);
		when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

		ResponseEntity<ErrorResponse> response = exceptionHandler.handleMethodArgumentNotValidException(exception);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getStatusCode()).isEqualTo(400);
		assertThat(response.getBody().getMessage()).isEqualTo("Bad Request");
		assertThat(response.getBody().getErrorDetails().size()).isEqualTo(2);
		assertThat(response.getBody().getErrorDetails().get(0)).isEqualTo("title: must not be null");
		assertThat(response.getBody().getErrorDetails().get(1)).isEqualTo("order: must not be null");
	}

	@Test
	void handleHttpMessageNotReadableException_returnsValidResponse() {
		HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
		when(exception.getMessage()).thenReturn("invalid json");
		when(exception.getLocalizedMessage()).thenReturn("invalid json");

		ResponseEntity<ErrorResponse> response = exceptionHandler.handleHttpMessageNotReadableException(exception);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getStatusCode()).isEqualTo(400);
		assertThat(response.getBody().getMessage()).isEqualTo("Bad Request");
		assertThat(response.getBody().getErrorDetails().size()).isEqualTo(1);
		assertThat(response.getBody().getErrorDetails().get(0)).isEqualTo("invalid json");
	}

	@Test
	void handleNoResourceFoundException_returnsValidResponse() {
		NoResourceFoundException exception = mock(NoResourceFoundException.class);
		when(exception.getMessage()).thenReturn("not found");
		when(exception.getLocalizedMessage()).thenReturn("not found");

		ResponseEntity<ErrorResponse> response = exceptionHandler.handleNoResourceFoundException(exception);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getStatusCode()).isEqualTo(404);
		assertThat(response.getBody().getMessage()).isEqualTo("not found");
		assertThat(response.getBody().getErrorDetails().size()).isEqualTo(0);
	}

	@Test
	void handleHttpRequestMethodNotSupportedException_returnsValidResponse() {
		HttpRequestMethodNotSupportedException exception = mock(HttpRequestMethodNotSupportedException.class);
		when(exception.getMessage()).thenReturn("method not allowed");
		when(exception.getLocalizedMessage()).thenReturn("method not allowed");

		ResponseEntity<ErrorResponse> response = exceptionHandler
			.handleHttpRequestMethodNotSupportedException(exception);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getStatusCode()).isEqualTo(405);
		assertThat(response.getBody().getMessage()).isEqualTo("method not allowed");
		assertThat(response.getBody().getErrorDetails().size()).isEqualTo(0);
	}

	@Test
	void handleHttpMediaTypeNotAcceptableException_returnsValidResponse() throws JsonProcessingException {
		HttpMediaTypeNotAcceptableException exception = mock(HttpMediaTypeNotAcceptableException.class);

		ResponseEntity<String> response = exceptionHandler.handleHttpMediaTypeNotAcceptableException(exception);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).isEqualTo(
				"{\"statusCode\":406,\"message\":\"acceptable MIME type: application/json\",\"errorDetails\":[]}");
	}

	@Test
	void handleHttpMediaTypeNotSupportedException_returnsValidResponse() {
		HttpMediaTypeNotSupportedException exception = mock(HttpMediaTypeNotSupportedException.class);
		when(exception.getMessage()).thenReturn("unsupported media type");
		when(exception.getLocalizedMessage()).thenReturn("unsupported media type");

		ResponseEntity<ErrorResponse> response = exceptionHandler.handleHttpMediaTypeNotSupportedException(exception);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getStatusCode()).isEqualTo(415);
		assertThat(response.getBody().getMessage()).isEqualTo("unsupported media type");
		assertThat(response.getBody().getErrorDetails().size()).isEqualTo(0);
	}

	@Test
	void handleException_returnsValidResponse() {
		Exception exception = mock(IllegalArgumentException.class);
		when(exception.getMessage()).thenReturn("encountered an exception");

		ResponseEntity<ErrorResponse> response = exceptionHandler.handleException(exception);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getStatusCode()).isEqualTo(500);
		assertThat(response.getBody().getMessage()).isEqualTo("error occurred");
		assertThat(response.getBody().getErrorDetails().size()).isEqualTo(0);
	}

}
