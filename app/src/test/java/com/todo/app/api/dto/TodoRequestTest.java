package com.todo.app.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TodoRequestTest {

	@Test
	void createsValidInstance() {
		TodoRequest todoRequest = new TodoRequest("title", false, 1);
		assertThat(todoRequest).isNotNull();
		assertThat(todoRequest.getTitle()).isEqualTo("title");
		assertThat(todoRequest.isCompleted()).isFalse();
		assertThat(todoRequest.getOrder()).isEqualTo(1);
	}

}
