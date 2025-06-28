package com.todo.app.service.impl;

import com.todo.app.core.Todo;
import com.todo.app.persistence.TodoRepository;
import com.todo.app.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Stubbing method calls, logic to be implemented
 */
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

	private final TodoRepository todoRepository;

	@Override
	public Todo createTodo(Todo todo) {
		return new Todo(1L, "test", false, 1);
	}

	@Override
	public Todo getTodo(String id) {
		return new Todo(1L, "test", false, 1);
	}

	@Override
	public List<Todo> getAllTodos() {
		return Arrays.asList(new Todo(1L, "test", false, 1), new Todo(2L, "test", false, 1));
	}

	@Override
	public Todo updateTodo(String id, Todo todo) {
		return new Todo(1L, "test", false, 1);
	}

	@Override
	public void deleteTodo(String id) {

	}

	@Override
	public void deleteAllTodos() {

	}

}
