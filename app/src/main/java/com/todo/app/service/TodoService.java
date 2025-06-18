package com.todo.app.service;

import com.todo.app.dto.Todo;

import java.util.List;

public interface TodoService {

	/**
	 * Create a new todo
	 * @param todo - the todo being created
	 * @return Todo, the created todo
	 */
	Todo createTodo(Todo todo);

	Todo getTodo(String id);

	List<Todo> getAllTodos();

	Todo updateTodo(String id, Todo todo);

	void deleteTodo(String id);

	void deleteAllTodos();

}
