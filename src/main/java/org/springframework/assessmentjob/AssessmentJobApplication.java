package org.springframework.assessmentjob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AssessmentJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssessmentJobApplication.class, args);
	}

	@Bean
	public Map<String, List<Integer>> report() {
		return new HashMap<>();
	}
}
