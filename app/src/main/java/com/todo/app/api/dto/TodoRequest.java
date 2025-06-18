package com.todo.app.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Valid
@Value
public class TodoRequest {

	@NotNull
	String title;

	boolean completed;

	@NotNull
	Integer order;

}
