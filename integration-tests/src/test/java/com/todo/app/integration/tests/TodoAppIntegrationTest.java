package com.todo.app.integration.tests;

import com.todo.app.integration.tests.openapi.api.TodoControllerApi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
		// will fail as database is not set up in test
		assertThrows(Exception.class, () -> todoControllerApi.getTodo(1L).block());
	}

}
