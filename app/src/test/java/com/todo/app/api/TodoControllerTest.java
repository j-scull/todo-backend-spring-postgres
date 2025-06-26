package com.todo.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.app.api.dto.TodoRequest;
import com.todo.app.api.dto.TodoResponse;
import com.todo.app.api.exception.GlobalExceptionHandler;
import com.todo.app.api.mapper.TodoMapper;
import com.todo.app.core.Todo;
import com.todo.app.service.TodoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
@Import(GlobalExceptionHandler.class)
class TodoControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	TodoService todoService;

	@MockitoBean
	TodoMapper todoMapper;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void createTodo_returnsTodoResponse() throws Exception {
		TodoRequest request = new TodoRequest("title", false, 1);
		Todo todo = new Todo(1L, "title", false, 1);
		TodoResponse response = TodoResponse.builder()
			.id(1L)
			.title("title")
			.completed(false)
			.order(1)
			.uri(new URI("/todos/1"))
			.build();
		when(todoMapper.fromRequest(request)).thenReturn(todo);
		when(todoService.createTodo(todo)).thenReturn(todo);
		when(todoMapper.toResponse(todo)).thenReturn(response);

		mockMvc
			.perform(post("/todos").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(content().json("""
					{
					  "id": 1,
					  "title": "title",
					  "completed": false,
					  "order": 1,
					  "uri": "/todos/1"
					}
					"""));

		verify(todoMapper).fromRequest(isA(TodoRequest.class));
		verify(todoService).createTodo(isA(Todo.class));
		verify(todoMapper).toResponse(isA(Todo.class));
	}

	@Test
	void createTodo_titleIsNull_returnsBadRequest() throws Exception {
		TodoRequest request = new TodoRequest(null, false, 1);

		mockMvc
			.perform(post("/todos").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(content().json("""
					 {
					   "statusCode": 400,
					   "message": "Bad Request",
					   "errorDetails": [
					     "title: must not be null"
					   ]
					 }
					"""));

		verify(todoMapper, times(0)).fromRequest(isA(TodoRequest.class));
		verify(todoService, times(0)).createTodo(isA(Todo.class));
		verify(todoMapper, times(0)).toResponse(isA(Todo.class));
	}

	@Test
	void createTodo_orderIsNull_returnsBadRequest() throws Exception {
		TodoRequest request = new TodoRequest("title", false, null);

		mockMvc
			.perform(post("/todos").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(content().json("""
					 {
					   "statusCode": 400,
					   "message": "Bad Request",
					   "errorDetails": [
					     "order: must not be null"
					   ]
					 }
					"""));

		verify(todoMapper, times(0)).fromRequest(isA(TodoRequest.class));
		verify(todoService, times(0)).createTodo(isA(Todo.class));
		verify(todoMapper, times(0)).toResponse(isA(Todo.class));
	}

	@Test
	void getTodo_todoExists_returnsTodoResponse() throws Exception {
		Todo todo = new Todo(1L, "title", false, 1);
		TodoResponse response = TodoResponse.builder()
			.id(1L)
			.title("title")
			.completed(false)
			.order(1)
			.uri(new URI("/todos/1"))
			.build();
		when(todoService.getTodo("1")).thenReturn(todo);
		when(todoMapper.toResponse(todo)).thenReturn(response);

		mockMvc.perform(get("/todos/1")).andExpect(status().isOk()).andExpect(content().json("""
				{
				  "id": 1,
				  "title": "title",
				  "completed": false,
				  "order": 1,
				  "uri": "/todos/1"
				}
				"""));

		verify(todoService).getTodo("1");
		verify(todoMapper).toResponse(isA(Todo.class));
	}

	@Test
	void getTodo_todoDoesNotExist_returnsNotFound() throws Exception {

		when(todoService.getTodo("1")).thenThrow(NoResourceFoundException.class);

		mockMvc.perform(get("/todos/1")).andExpect(status().isNotFound()).andExpect(content().json("""
				{
				  "statusCode": 404,
				  "message": null,
				  "errorDetails": []
				}
				"""));

		verify(todoService).getTodo("1");
		verify(todoMapper, times(0)).toResponse(isA(Todo.class));
	}

	@Test
	void getAllTodos_returnsTodoResponseList() throws Exception {
		Todo todo1 = new Todo(1L, "title", false, 1);
		Todo todo2 = new Todo(2L, "title", false, 2);
		TodoResponse response1 = TodoResponse.builder()
			.id(1L)
			.title("title")
			.completed(false)
			.order(1)
			.uri(new URI("/todos/1"))
			.build();
		TodoResponse response2 = TodoResponse.builder()
			.id(2L)
			.title("title")
			.completed(false)
			.order(2)
			.uri(new URI("/todos/2"))
			.build();
		List<Todo> todoList = List.of(todo1, todo2);
		when(todoService.getAllTodos()).thenReturn(todoList);
		when(todoMapper.toResponse(todo1)).thenReturn(response1);
		when(todoMapper.toResponse(todo2)).thenReturn(response2);

		mockMvc.perform(get("/todos")).andExpect(status().isOk()).andExpect(content().json("""
				[
					{
					  "id": 1,
					  "title": "title",
					  "completed": false,
					  "order": 1,
					  "uri": "/todos/1"
					},
					{
					  "id": 2,
					  "title": "title",
					  "completed": false,
					  "order": 2,
					  "uri": "/todos/2"
					}
				]
				"""));

		verify(todoService).getAllTodos();
		verify(todoMapper, times(2)).toResponse(isA(Todo.class));
	}

	@Test
	void getAllTodos_notTodosExist_returnsEmptyList() throws Exception {
		when(todoService.getAllTodos()).thenReturn(new ArrayList<>());

		mockMvc.perform(get("/todos")).andExpect(status().isOk()).andExpect(content().json("[]"));

		verify(todoService).getAllTodos();
		verify(todoMapper, times(0)).toResponse(isA(Todo.class));
	}

	@Test
	void updateTodo_returnsUpdatedTodo() throws Exception {
		TodoRequest request = new TodoRequest("title", false, 1);
		Todo todo = new Todo(1L, "title", false, 1);
		TodoResponse response = TodoResponse.builder()
			.id(1L)
			.title("title")
			.completed(false)
			.order(1)
			.uri(new URI("/todos/1"))
			.build();
		when(todoMapper.fromRequest(request)).thenReturn(todo);
		when(todoService.updateTodo("1", todo)).thenReturn(todo);
		when(todoMapper.toResponse(todo)).thenReturn(response);

		mockMvc
			.perform(patch("/todos/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(content().json("""
					{
					  "id": 1,
					  "title": "title",
					  "completed": false,
					  "order": 1,
					  "uri": "/todos/1"
					}
					"""));

		verify(todoMapper).fromRequest(isA(TodoRequest.class));
		verify(todoService).updateTodo(isA(String.class), isA(Todo.class));
		verify(todoMapper).toResponse(isA(Todo.class));
	}

	@Test
	void deleteTodo_successfulResponse() throws Exception {
		doNothing().when(todoService).deleteTodo("1");

		mockMvc.perform(delete("/todos/1")).andExpect(status().isNoContent());

		verify(todoService).deleteTodo("1");
	}

	@Test
	void deleteTodo_todoNotFound_returnsNotFound() throws Exception {
		doThrow(NoResourceFoundException.class).when(todoService).deleteTodo("1");

		mockMvc.perform(delete("/todos/1")).andExpect(status().isNotFound()).andExpect(content().json("""
				{
				  "statusCode": 404,
				  "message": null,
				  "errorDetails": []
				}
				"""));

		verify(todoService).deleteTodo("1");

	}

	@Test
	void deleteAllTodos_successfulResponse() throws Exception {
		doNothing().when(todoService).deleteAllTodos();

		mockMvc.perform(delete("/todos")).andExpect(status().isNoContent());

		verify(todoService).deleteAllTodos();
	}

}
