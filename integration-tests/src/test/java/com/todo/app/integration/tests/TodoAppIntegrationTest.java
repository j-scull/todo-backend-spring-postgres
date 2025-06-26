package com.todo.app.integration.tests;

import com.todo.app.integration.tests.openapi.api.TodoControllerApi;
import com.todo.app.integration.tests.openapi.dto.TodoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

class TodoAppIntegrationTest extends AbstractBaseIntegrationTest {

	private final TodoControllerApi todoControllerApi = new TodoControllerApi();

	@LocalServerPort
	private int port;

	@BeforeEach
	void setup() {
		todoControllerApi.getApiClient().setBasePath("http://localhost:" + port);
	}

	@Test
	void getTodo_returnsTodo() {
		// stubbed response from TodoService
		TodoResponse todoResponse = todoControllerApi.getTodo("1").block();
		assertThat(todoResponse).isNotNull();
	}

}
