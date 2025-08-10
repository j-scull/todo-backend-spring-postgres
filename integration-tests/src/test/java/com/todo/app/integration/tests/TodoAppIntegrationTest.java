package com.todo.app.integration.tests;

import com.todo.app.TodoBackendSpringPostgresApp;
import com.todo.app.integration.tests.openapi.api.TodoControllerApi;

import com.todo.app.integration.tests.openapi.dto.TodoRequest;
import com.todo.app.integration.tests.openapi.dto.TodoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ExtendWith({ SpringExtension.class, OutputCaptureExtension.class })
@SpringBootTest(classes = { TodoBackendSpringPostgresApp.class },
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = { TodoAppIntegrationTest.Initializer.class })
@Testcontainers
class TodoAppIntegrationTest {

	@Container
	public static PostgreSQLContainer postgreSqlContainer = new PostgreSQLContainer("postgres:11.1")
		.withDatabaseName("todo_db")
		.withUsername("admin")
		.withPassword("password");

	private final TodoControllerApi todoControllerApi = new TodoControllerApi();

	@LocalServerPort
	private int port;

	@BeforeEach
	void setup() {
		todoControllerApi.getApiClient().setBasePath("http://localhost:" + port);
	}

	@AfterEach
	void afterEach() throws IOException, InterruptedException {
		// todoControllerApi.deleteAllTodos();
		postgreSqlContainer.execInContainer("TRUNCATE table_name; DELETE FROM table_name;");

	}

	@Order(0)
	@Test
	void testDatabaseIsRunning() {
		assertThat(postgreSqlContainer.isRunning()).isTrue();
	}

	@Order(1)
	@Test
	void getTodo_todoNotFound_returnsNotFoundException(CapturedOutput output) {
		assertThrows(WebClientResponseException.NotFound.class, () -> todoControllerApi.getTodo(1L).block());
		assertTrue(output.getOut().contains("Not found: No static resource /todos/1."));
	}

	@Test
	void getTodo_todoFound_returnsValidTodo() {
		TodoRequest todoRequest = new TodoRequest().title("test").completed(false).order(1);

		TodoResponse createResponse = todoControllerApi.createTodo(todoRequest).block();
		TodoResponse getResponse = todoControllerApi.getTodo(1L).block();

		assertThat(getResponse).isNotNull();
		assertThat(createResponse).isEqualTo(getResponse);
		assertThat(getResponse).returns(1L, TodoResponse::getId)
			.returns("test", TodoResponse::getTitle)
			.returns(false, TodoResponse::getCompleted)
			.returns(1, TodoResponse::getOrder)
			.returns(URI.create("http://localhost:" + port + "/todos/1"), TodoResponse::getUri);
	}

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues
				.of("spring.datasource.url=" + postgreSqlContainer.getJdbcUrl(),
						"spring.datasource.username=" + postgreSqlContainer.getUsername(),
						"spring.datasource.password=" + postgreSqlContainer.getPassword())
				.applyTo(configurableApplicationContext.getEnvironment());
		}

	}

}
