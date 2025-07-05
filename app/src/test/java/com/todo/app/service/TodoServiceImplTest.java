package com.todo.app.service;

import com.todo.app.core.Todo;
import com.todo.app.persistence.TodoRepository;
import com.todo.app.service.impl.TodoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    void createTodo_todoIsValid_savesTodo() {
        Todo todo = new Todo("title", false, 1);
        Todo savedTodo = new Todo(1L,"title", false, 1);

        when(todoRepository.save(todo)).thenReturn(savedTodo);

        Todo result = todoService.createTodo(todo);

        assertThat(result).isEqualTo(savedTodo);
    }

    @Test
    void getTodo_todoExists_returnsTodo() throws NoResourceFoundException {
        Todo todo = new Todo(1L, "title", false, 1);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        Todo result = todoService.getTodo(1L);

        assertThat(result).isEqualTo(todo);
    }

    @Test
    void getTodo_todoDoesNotExists_throwsException() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoResourceFoundException.class, () ->  todoService.getTodo(1L));
    }

    @Test
    void getTodos_todosExist_returnsTodoList() {
        List<Todo> todoList = List.of(
                new Todo(1L, "title", false, 1),
                new Todo(2L, "title", true, 2));

        when(todoRepository.findAll()).thenReturn(todoList);

        List<Todo> result = todoService.getAllTodos();

        assertThat(result).isEqualTo(todoList);
    }

    @Test
    void getTodos_noTodosExist_returnsEmptyList() {
        when(todoRepository.findAll()).thenReturn(List.of());

        List<Todo> result = todoService.getAllTodos();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void updateTodo_todoExists_returnsTodo() throws NoResourceFoundException {
        Todo oldTodo = new Todo(1L, "title", false, 1);
        Todo todoUpdate = mock(Todo.class);
        Todo updatedTodo = new Todo(1L, "title", true, 1);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(oldTodo));
        when(todoRepository.save(todoUpdate)).thenReturn(updatedTodo);

        Todo result = todoService.updateTodo(1L, todoUpdate);

        assertThat(result).isEqualTo(updatedTodo);
        verify(todoUpdate).setId(1L);
    }

    @Test
    void updateTodo_todoDoesNotExists_throwsException() {
        Todo todoUpdate = mock(Todo.class);

        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoResourceFoundException.class, () -> todoService.updateTodo(1L, todoUpdate));

        verifyNoInteractions(todoUpdate);
    }

    @Test
    void deleteTodo_todoExists_completesSuccessfully() throws NoResourceFoundException {
        Todo todo = new Todo(1L, "title", false, 1);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        doNothing().when(todoRepository).deleteById(1L);

        todoService.deleteTodo(1L);

        verify(todoRepository).deleteById(1L);
    }

    @Test
    void deleteTodo_todoDoesNotExists_throwsException() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoResourceFoundException.class, () ->  todoService.deleteTodo(1L));
    }

    @Test
    void deleteTodos_todoExists_completesSuccessfully() {
        doNothing().when(todoRepository).deleteAll();

        todoService.deleteAllTodos();

        verify(todoRepository).deleteAll();
    }
}
