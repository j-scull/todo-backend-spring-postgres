package com.todo.app.api;

import com.todo.app.api.dto.TodoRequest;
import com.todo.app.api.dto.TodoResponse;
import com.todo.app.api.mapper.TodoMapper;
import com.todo.app.core.Todo;
import com.todo.app.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The REST controller for the Todo api
 */
@Slf4j
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

	private final TodoService todoService;

	private final TodoMapper todoMapper;

	@PostMapping
	public @ResponseStatus(HttpStatus.CREATED) @ResponseBody TodoResponse createTodo(
			@Valid @RequestBody final TodoRequest newTodo, HttpServletRequest request) {
		log.info("Creating todo");
		Todo todo = todoService.createTodo(todoMapper.fromRequest(newTodo));
		return todoMapper.toResponse(todo);
	}

	@GetMapping("/{id}")
	public @ResponseBody TodoResponse getTodo(@PathVariable String id, HttpServletRequest request) {
		log.info("Get todo with id: {}", id);
		Todo todo = todoService.getTodo(id);
		return todoMapper.toResponse(todo);
	}

	@GetMapping
	public @ResponseBody List<TodoResponse> getAllTodos(HttpServletRequest request) {
		log.info("Get all todos");
		List<Todo> todos = todoService.getAllTodos();
		return todos.stream().map(todoMapper::toResponse).collect(Collectors.toList());
	}

	@PatchMapping("/{id}")
	public @ResponseBody TodoResponse updateTodo(@PathVariable String id, @RequestBody final TodoRequest todoUpdate,
			HttpServletRequest request) {
		log.info("Updating todo with id: {}", id);
		Todo updatedTodo = todoService.updateTodo(id, todoMapper.fromRequest(todoUpdate));
		return todoMapper.toResponse(updatedTodo);
	}

	@DeleteMapping("/{id}")
	public @ResponseStatus(HttpStatus.NO_CONTENT) void deleteTodo(@PathVariable String id) {
		log.info("Deleting todo with id: {}", id);
		todoService.deleteTodo(id);
	}

	@DeleteMapping
	public @ResponseStatus(HttpStatus.NO_CONTENT) void deleteAllTodos() {
		log.info("Delete all todos");
		todoService.deleteAllTodos();
	}

}
