package com.todo.app.api.mapper;

import com.todo.app.api.dto.TodoRequest;
import com.todo.app.api.dto.TodoResponse;
import com.todo.app.core.Todo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TodoMapperTest {

	TodoMapper todoMapper = new TodoMapper();

	@Test
	void toResponse_createsValidInstance() {
		Todo todo = new Todo(1L, "title", false, 1);

		TodoResponse todoResponse = todoMapper.toResponse(todo);

		assertThat(todoResponse).isNotNull();
		assertThat(todoResponse.getId()).isEqualTo(1L);
		assertThat(todoResponse.getTitle()).isEqualTo("title");
		assertThat(todoResponse.isCompleted()).isFalse();
		assertThat(todoResponse.getOrder()).isEqualTo(1L);
		assertThat(todoResponse.getUri().getPath()).isEqualTo("/todos/1");
	}

	@Test
	void toResponse_nullTodo_returnsNull() {
		TodoResponse todoResponse = todoMapper.toResponse(null);

		assertThat(todoResponse).isNull();
	}

	@Test
	void fromRequest_createsValidInstance() {
		TodoRequest todoRequest = new TodoRequest("title", false, 1);
		Todo todo = todoMapper.fromRequest(todoRequest);
		assertThat(todo).isNotNull();
		assertThat(todo.getId()).isNull();
		assertThat(todo.getTitle()).isEqualTo("title");
		assertThat(todo.isCompleted()).isFalse();
		assertThat(todo.getOrder()).isEqualTo(1L);
	}

	@Test
	void fromRequest_nullRequest_returnsNull() {
		Todo todo = todoMapper.fromRequest(null);

		assertThat(todo).isNull();
	}

}
