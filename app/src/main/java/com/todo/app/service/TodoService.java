package com.todo.app.service;

import com.todo.app.core.Todo;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

public interface TodoService {

	/**
	 * Create a new todo
	 * @param todo - the todo being created
	 * @return Todo, the created todo
	 */
	Todo createTodo(Todo todo);

	/**
	 * Get a Todo by id
	 * @param id - the id associated with the Todo
	 * @return a Todo
	 */
	Todo getTodo(long id) throws NoResourceFoundException;

	/**
	 * Get all Todos
	 * @return an Iterable of all Todos
	 */
	List<Todo> getAllTodos();

	/**
	 * Update a Todo
	 * @param id - the id associated with the Todo
	 * @param todo - the updated Todo content
	 * @return the updated Todo
	 */
	Todo updateTodo(long id, Todo todo) throws NoResourceFoundException;

	/**
	 * Delete a Todo by id
	 * @param id - the id associated with the Todo
	 */
	void deleteTodo(long id) throws NoResourceFoundException;

	/**
	 * Delete all Todos
	 */
	void deleteAllTodos();

}
