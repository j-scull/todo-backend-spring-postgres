package com.todo.app.integration.tests;

import com.todo.app.TodoBackendSpringPostgresApp;
import com.todo.app.integration.tests.openapi.api.TodoControllerApi;

import com.todo.app.integration.tests.openapi.dto.TodoRequest;
import com.todo.app.integration.tests.openapi.dto.TodoResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ExtendWith({ SpringExtension.class, OutputCaptureExtension.class })
@SpringBootTest(classes = { TodoBackendSpringPostgresApp.class },
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = { TodoAppIntegrationTest.Initializer.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
		// org.testcontainers.containers.Container.ExecResult res =
		// postgreSqlContainer.execInContainer("psql -U admin -d todo_db -c \"TRUNCATE
		// todos; DELETE FROM todos;\"");
		todoControllerApi.deleteAllTodos().block();
	}

	@Order(1)
	@Test
	void testDatabaseIsRunning() {
		assertThat(postgreSqlContainer.isRunning()).isTrue();
	}

	@Order(2)
	@ParameterizedTest
	@ValueSource(longs = { 1, 2, 3 })
	void createTodo_createsNewTodo_returnsValidTodoWithIncrementedId(long expectedId) {
		TodoRequest todoRequest = new TodoRequest().title("test").completed(false).order(1);
		TodoResponse createResponse = todoControllerApi.createTodo(todoRequest).block();
		assertThat(createResponse).isNotNull()
			.returns(expectedId, TodoResponse::getId)
			.returns("test", TodoResponse::getTitle)
			.returns(false, TodoResponse::getCompleted)
			.returns(1, TodoResponse::getOrder)
			.returns(URI.create("http://localhost:" + port + "/todos/" + expectedId), TodoResponse::getUri);
	}

	@Test
	void getTodo_todoNotFound_returnsNotFoundException(CapturedOutput output) {
		assertThrows(WebClientResponseException.NotFound.class, () -> todoControllerApi.getTodo(1L).block());
		assertTrue(output.getOut().contains("Not found: No static resource /todos/1."));
	}

	@Test
	void getTodo_todoFound_returnsValidTodo() {
		TodoRequest todoRequest = new TodoRequest().title("test").completed(false).order(1);

		TodoResponse createResponse = todoControllerApi.createTodo(todoRequest).block();
		TodoResponse getResponse = todoControllerApi.getTodo(createResponse.getId()).block();

		assertThat(createResponse).isNotNull();
		assertThat(getResponse).isNotNull();
		assertThat(createResponse).isEqualTo(getResponse);
		assertThat(getResponse).returns(createResponse.getId(), TodoResponse::getId)
			.returns("test", TodoResponse::getTitle)
			.returns(false, TodoResponse::getCompleted)
			.returns(1, TodoResponse::getOrder)
			.returns(URI.create("http://localhost:" + port + "/todos/" + createResponse.getId()), TodoResponse::getUri);
	}

	@Test
	void getAlTodos_noTodosFound_returnsEmptyOkResponse() {
		List<TodoResponse> getAllResponse = todoControllerApi.getAllTodos().collectList().block();

		assertThat(getAllResponse).isNotNull();
		assertThat(getAllResponse.size()).isEqualTo(0);
	}

	@Test
	void getAlTodos_twoTodosFound_returnsValidTodoList() {
		TodoRequest todoRequest1 = new TodoRequest().title("test1").completed(false).order(1);
		TodoRequest todoRequest2 = new TodoRequest().title("test2").completed(false).order(2);

		TodoResponse createResponse1 = todoControllerApi.createTodo(todoRequest1).block();
		TodoResponse createResponse2 = todoControllerApi.createTodo(todoRequest2).block();
		List<TodoResponse> getAllResponse = todoControllerApi.getAllTodos().collectList().block();

		assertThat(createResponse1).isNotNull();
		assertThat(createResponse2).isNotNull();
		assertThat(getAllResponse).isNotNull();
		assertThat(getAllResponse.size()).isEqualTo(2);
		List<TodoResponse> sortedResponse = getAllResponse.stream()
			.sorted(Comparator.comparing(TodoResponse::getId))
			.toList();
		assertThat(sortedResponse.get(0)).returns(createResponse1.getId(), TodoResponse::getId)
			.returns("test1", TodoResponse::getTitle)
			.returns(false, TodoResponse::getCompleted)
			.returns(1, TodoResponse::getOrder)
			.returns(URI.create("http://localhost:" + port + "/todos/" + createResponse1.getId()),
					TodoResponse::getUri);
		assertThat(sortedResponse.get(1)).returns(createResponse2.getId(), TodoResponse::getId)
			.returns("test2", TodoResponse::getTitle)
			.returns(false, TodoResponse::getCompleted)
			.returns(2, TodoResponse::getOrder)
			.returns(URI.create("http://localhost:" + port + "/todos/" + createResponse2.getId()),
					TodoResponse::getUri);
	}

	@Test
	void updateTodo_todoNotFound_returnsNotFoundException(CapturedOutput output) {
		TodoRequest todoRequest = new TodoRequest().title("test1").completed(false).order(1);
		assertThrows(WebClientResponseException.NotFound.class,
				() -> todoControllerApi.updateTodo(1L, todoRequest).block());
		assertTrue(output.getOut().contains("Not found: No static resource /todos/1."));
	}

	@Test
	void updateTodo_todoFound_returnsUpdatedTodo(CapturedOutput output) {
		// create initial entry
		TodoRequest createRequest = new TodoRequest().title("test1").completed(false).order(1);
		TodoResponse createResponse = todoControllerApi.createTodo(createRequest).block();

		assertThat(createResponse).isNotNull()
			.returns("test1", TodoResponse::getTitle)
			.returns(false, TodoResponse::getCompleted)
			.returns(1, TodoResponse::getOrder);

		// update the entry
		TodoRequest updateRequest = new TodoRequest().title("test2").completed(true).order(2);
		TodoResponse updateResponse = todoControllerApi.updateTodo(createResponse.getId(), updateRequest).block();

		assertThat(updateResponse).isNotNull()
			.returns(createResponse.getId(), TodoResponse::getId)
			.returns("test2", TodoResponse::getTitle)
			.returns(true, TodoResponse::getCompleted)
			.returns(2, TodoResponse::getOrder)
			.returns(URI.create("http://localhost:" + port + "/todos/" + createResponse.getId()), TodoResponse::getUri);

		// perform a get request to confirm the update
		TodoResponse getResponse = todoControllerApi.getTodo(createResponse.getId()).block();

		assertThat(getResponse).isNotNull()
			.returns(createResponse.getId(), TodoResponse::getId)
			.returns("test2", TodoResponse::getTitle)
			.returns(true, TodoResponse::getCompleted)
			.returns(2, TodoResponse::getOrder)
			.returns(URI.create("http://localhost:" + port + "/todos/" + createResponse.getId()), TodoResponse::getUri);
	}

	@Test
	void deleteTodo_todoNotFound_returnsNotFoundException(CapturedOutput output) {
		assertThrows(WebClientResponseException.NotFound.class, () -> todoControllerApi.deleteTodo(1L).block());
		assertTrue(output.getOut().contains("Not found: No static resource /todos/1."));
	}

	@Test
	void deleteTodo_todoFound_removesTodoFromDatabase(CapturedOutput output) {
		// create an entry and confirm it exists
		TodoRequest todoRequest = new TodoRequest().title("test").completed(false).order(1);

		TodoResponse createResponse = todoControllerApi.createTodo(todoRequest).block();
		TodoResponse getResponse = todoControllerApi.getTodo(createResponse.getId()).block();

		assertThat(createResponse).isNotNull();
		assertThat(getResponse).isNotNull();
		assertThat(createResponse).isEqualTo(getResponse);
		assertThat(getResponse).returns(createResponse.getId(), TodoResponse::getId)
			.returns("test", TodoResponse::getTitle)
			.returns(false, TodoResponse::getCompleted)
			.returns(1, TodoResponse::getOrder)
			.returns(URI.create("http://localhost:" + port + "/todos/" + createResponse.getId()), TodoResponse::getUri);

		// delete the entry
		todoControllerApi.deleteTodo(createResponse.getId()).block();

		// confirm the entry is deleted
		assertThrows(WebClientResponseException.NotFound.class,
				() -> todoControllerApi.getTodo(createResponse.getId()).block());
		assertTrue(output.getOut().contains("Not found: No static resource /todos/" + createResponse.getId()));
	}

	@Test
	void deleteAllTodos_noTodosFound_returnsEmptyOkResponse() {
		assertDoesNotThrow(() -> todoControllerApi.deleteAllTodos().block());
	}

	@Test
	void deleteAlTodos_todosExist_removesAllTodos() {
		// create todos and confirm they exist in the database
		TodoRequest todoRequest1 = new TodoRequest().title("test1").completed(false).order(1);
		TodoRequest todoRequest2 = new TodoRequest().title("test2").completed(false).order(2);

		TodoResponse createResponse1 = todoControllerApi.createTodo(todoRequest1).block();
		TodoResponse createResponse2 = todoControllerApi.createTodo(todoRequest2).block();
		List<TodoResponse> getAllResponse1 = todoControllerApi.getAllTodos().collectList().block();

		assertThat(createResponse1).isNotNull();
		assertThat(createResponse2).isNotNull();
		assertThat(getAllResponse1).isNotNull();
		assertThat(getAllResponse1.size()).isEqualTo(2);

		// delete all todos
		todoControllerApi.deleteAllTodos().block();

		// confirm all todos have been deleted
		List<TodoResponse> getAllResponse2 = todoControllerApi.getAllTodos().collectList().block();
		assertThat(getAllResponse2).isNotNull();
		assertThat(getAllResponse2.size()).isEqualTo(0);
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
