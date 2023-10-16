package com.CVP.cv_project;

import com.CVP.cv_project.services.CVService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CvProjectApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


	public static void main(String[] args) {
		SpringApplication.run(CvProjectApplication.class, args);
		CVService cvSrvc = new CVService();
		cvSrvc.checkIfTime();
	}

}
