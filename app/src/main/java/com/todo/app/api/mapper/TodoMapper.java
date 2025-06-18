package com.todo.app.api.mapper;

import com.todo.app.dto.TodoRequest;
import com.todo.app.dto.TodoResponse;
import com.todo.app.dto.Todo;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class TodoMapper {

	public TodoResponse toResponse(Todo todo) {
		if (todo == null) {
			return null;
		}
		return TodoResponse.builder()
			.id(todo.getId())
			.title(todo.getTitle())
			.completed(todo.isCompleted())
			.order(todo.getOrder())
			.uri(URI.create("/" + todo.getId()))
			.build();
	}

	public Todo fromRequest(TodoRequest request) {
		if (request == null) {
			return null;
		}
		return new Todo(null, request.getTitle(), request.isCompleted(), request.getOrder());
	}

}
