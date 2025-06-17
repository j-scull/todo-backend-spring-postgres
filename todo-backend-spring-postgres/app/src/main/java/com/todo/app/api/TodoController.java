package com.todo.app.api;

import com.todo.app.dto.TodoRequest;
import com.todo.app.dto.TodoResponse;
import com.todo.app.api.mapper.TodoMapper;
import com.todo.app.dto.Todo;
import com.todo.app.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The REST controller for the todo api
 */
@Slf4j
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    private final TodoMapper todoMapper;

    @RequestMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello!");
    }


    // create todo
    // POST /{id}
    @PostMapping("/{id}")
    public @ResponseBody TodoResponse createTodo(@PathVariable String id, @RequestBody final TodoRequest todoRequest, HttpServletRequest request) {
        log.info("creating todo with id: {}", id);
        Todo todo = todoMapper.fromRequest(todoRequest);
        // todo call service to store dto
        return todoMapper.toResponse(todo);
    }


    // view a todo
    // GET /{id}

    // update a todo
    // PATH /{id}

    // get all todos
    // GET /

    // DELETE a todo
    // DELETE /{id}

    // DELETE all todos
    // DELETE /


}
