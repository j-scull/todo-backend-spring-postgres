package com.todo.app.integration.tests;

import com.todo.app.TodoBackendSpringPostgresApp;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { TodoBackendSpringPostgresApp.class },
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractBaseIntegrationTest {

}
