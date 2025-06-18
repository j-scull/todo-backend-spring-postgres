package com.todo.app.api.dto;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TodoResponseTest {

	@Test
	void createsValidInstance() {
		TodoResponse todoResponse = TodoResponse.builder()
			.id(1L)
			.title("title")
			.completed(false)
			.order(1)
			.uri(URI.create("/"))
			.build();

		assertThat(todoResponse).isNotNull();
		assertThat(todoResponse.getId()).isEqualTo(1L);
		assertThat(todoResponse.getTitle()).isEqualTo("title");
		assertThat(todoResponse.isCompleted()).isFalse();
		assertThat(todoResponse.getOrder()).isEqualTo(1);
		assertThat(todoResponse.getUri().getPath()).isEqualTo("/");

	}

}
