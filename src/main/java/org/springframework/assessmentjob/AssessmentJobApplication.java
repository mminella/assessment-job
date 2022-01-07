package org.springframework.assessmentjob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.assessmentjob.configuration.ProjectAssessmentProperties;
import org.springframework.assessmentjob.util.ReportKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(ProjectAssessmentProperties.class)
public class AssessmentJobApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(AssessmentJobApplication.class, args);
		SpringApplication.exit(applicationContext);
		System.exit(0);
	}

	@Bean
	public Map<ReportKey, List<Long>> report() {
		return new HashMap<>();
	}
}
