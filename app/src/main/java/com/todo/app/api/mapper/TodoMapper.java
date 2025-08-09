package com.todo.app.api.mapper;

import com.todo.app.api.TodoController;
import com.todo.app.api.dto.TodoRequest;
import com.todo.app.api.dto.TodoResponse;
import com.todo.app.core.dto.Todo;
import org.springframework.stereotype.Component;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class TodoMapper {

	public TodoResponse toResponse(Todo todo) {
		if (todo == null) {
			return null;
		}
		return TodoResponse.builder()
			.id(todo.getId())
			.title(todo.getTodoTitle())
			.completed(todo.getTodoIsCompleted())
			.order(todo.getTodoOrder())
			.uri(URI.create(linkTo(TodoController.class).slash(todo.getId()).withSelfRel().getHref()))
			.build();
	}

	public Todo fromRequest(TodoRequest request) {
		if (request == null) {
			return null;
		}
		return new Todo(request.getTitle(), request.getCompleted(), request.getOrder());
	}

}
