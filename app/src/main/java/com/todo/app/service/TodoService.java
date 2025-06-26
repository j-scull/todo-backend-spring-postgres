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
	 * @throws NoResourceFoundException if no Todo matches the provided is
	 */
	Todo getTodo(String id) throws NoResourceFoundException;

	/**
	 * Get all Todos
	 * @return a List of all Todos
	 */
	List<Todo> getAllTodos();

	/**
	 * Update a Todo
	 * @param id - the id associated with the Todo
	 * @param todo - the updated Todo content
	 * @return the updated Todo
	 */
	Todo updateTodo(String id, Todo todo);

	/**
	 * Delete a Todo by id
	 * @param id - the id associated with the Todo
	 * @throws NoResourceFoundException if no Todo matches the provided is
	 */
	void deleteTodo(String id) throws NoResourceFoundException;

	/**
	 * Delete all Todos
	 */
	void deleteAllTodos();

}
