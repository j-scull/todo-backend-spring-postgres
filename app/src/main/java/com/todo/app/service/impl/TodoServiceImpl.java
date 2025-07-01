package com.todo.app.service.impl;

import com.todo.app.api.TodoController;
import com.todo.app.core.Todo;
import com.todo.app.persistence.TodoRepository;
import com.todo.app.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Stubbing method calls, logic to be implemented
 */
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

	private final TodoRepository todoRepository;

	private final String RESOURCE_PATH = TodoController.BASE_PATH + "/%d";

	@Override
	public Todo createTodo(Todo todo) {
		return todoRepository.save(todo);
	}

	@Override
	public Todo getTodo(long id) throws NoResourceFoundException {
		return todoRepository.findById(id)
			.orElseThrow(() -> new NoResourceFoundException(HttpMethod.GET, String.format(RESOURCE_PATH, id)));
	}

	@Override
	public List<Todo> getAllTodos() {
		List<Todo> todoList = new ArrayList<>();
		todoRepository.findAll().forEach(todoList::add);
		return todoList;
	}

	@Override
	public Todo updateTodo(long id, Todo todo) throws NoResourceFoundException {
		todoRepository.findById(id)
			.orElseThrow(() -> new NoResourceFoundException(HttpMethod.PATCH, String.format(RESOURCE_PATH, id)));
		todo.setId(id);
		return todoRepository.save(todo);
	}

	@Override
	public void deleteTodo(long id) throws NoResourceFoundException {
		todoRepository.findById(id)
			.orElseThrow(() -> new NoResourceFoundException(HttpMethod.DELETE, String.format(RESOURCE_PATH, id)));
		todoRepository.deleteById(id);
	}

	@Override
	public void deleteAllTodos() {
		todoRepository.deleteAll();
	}

}
