package com.todo.app.service;

import com.todo.app.dto.Todo;

public interface TodoService {


    /**
     * Create a new todo
     * @param todo - the todo being created
     * @return Todo, the created todo
     */
    Todo createTodo(Todo todo);


}
