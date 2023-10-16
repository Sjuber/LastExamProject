package com.CVP.cv_project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@EnableJpaRepositories(basePackages = {"com.CVP.cv_project"})
class CvProjectApplicationTests {

	@Test
	void contextLoads() {
	}

}
