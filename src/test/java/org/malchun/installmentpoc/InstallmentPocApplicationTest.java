package org.malchun.installmentpoc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


@SpringBootTest
public class InstallmentPocApplicationTest {

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
			"postgres:15-alpine"
	);

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}


	@BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

	@Test
	void contextLoads() {
	}
}
