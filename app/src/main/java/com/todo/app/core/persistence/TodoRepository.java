package com.todo.app.core.persistence;

import com.todo.app.core.dto.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Long> {

}
