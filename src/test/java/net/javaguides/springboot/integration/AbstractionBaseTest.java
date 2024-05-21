package net.javaguides.springboot.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractionBaseTest {
	
	static final MySQLContainer MY_SQL_CONTAINER;
	
	static {
		MY_SQL_CONTAINER = new MySQLContainer("mysql:latest") // make sure to download docker in your machine
				.withUsername("username")
                .withPassword("password")
                .withDatabaseName("ems");
		
		MY_SQL_CONTAINER.start(); // here we re explicitlly starting the test container, we will not use @TestContainer to manage the lifecycle.
	}
	
	
	@DynamicPropertySource // this will dynamically fetch values from MySQLcontainer and will add to the application context.
	public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
	}

}
