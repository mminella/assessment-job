/*
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.assessmentjob.configuration;

import org.springframework.assessmentjob.batch.ClosedAsBackportPerMonthTasklet;
import org.springframework.assessmentjob.batch.ClosedAsBugPerMonthTasklet;
import org.springframework.assessmentjob.batch.ClosedAsDocumentationPerMonthTasklet;
import org.springframework.assessmentjob.batch.ClosedAsDuplicatePerMonthTasklet;
import org.springframework.assessmentjob.batch.ClosedAsEnhancementPerMonthTasklet;
import org.springframework.assessmentjob.batch.ClosedAsInvalidPerMonthTasklet;
import org.springframework.assessmentjob.batch.ClosedAsQuestionPerMonthTasklet;
import org.springframework.assessmentjob.batch.ClosedAsTaskPerMonthTasklet;
import org.springframework.assessmentjob.batch.CommunityCreatedPerMonthTasklet;
import org.springframework.assessmentjob.batch.MonthlyDownloadCountTasklet;
import org.springframework.assessmentjob.batch.NotTriagedPerMonthTasklet;
import org.springframework.assessmentjob.batch.ProjectCreationPerMonthTasklet;
import org.springframework.assessmentjob.batch.ReportGeneratingTasklet;
import org.springframework.assessmentjob.batch.TeamCreatedPerMonthTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Minella
 */
@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

	private final JobBuilderFactory jobBuilderFactory;

	private final StepBuilderFactory stepBuilderFactory;

	public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Step teamCreatedPerMonthStep(TeamCreatedPerMonthTasklet tasklet) {
		return this.stepBuilderFactory.get("teamCreatedPerMonthStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step communityCreatedPerMonthStep(CommunityCreatedPerMonthTasklet tasklet) {
		return this.stepBuilderFactory.get("communityCreatedPerMonthStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step closedAsDuplicatePerMonthStep(ClosedAsDuplicatePerMonthTasklet tasklet) {
		return this.stepBuilderFactory.get("closedAsDuplicatePerMonthStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step closedAsInvalidPerMonthStep(ClosedAsInvalidPerMonthTasklet tasklet) {
		return this.stepBuilderFactory.get("closedAsInvalidPerMonthStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step closedAsEnhancementPerMonthStep(ClosedAsEnhancementPerMonthTasklet tasklet) {
		return this.stepBuilderFactory.get("closedAsEnhancementPerMonthStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step closedAsBugPerMonthStep(ClosedAsBugPerMonthTasklet tasklet) {
		return this.stepBuilderFactory.get("closedAsBugPerMonthStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step closedAsTaskPerMonthStep(ClosedAsTaskPerMonthTasklet tasklet) {
		return this.stepBuilderFactory.get("closedAsTaskPerMonthStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step closedAsDocumentationPerMonthStep(ClosedAsDocumentationPerMonthTasklet tasklet) {
		return this.stepBuilderFactory.get("closedAsDocumentationPerMonthStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step closedAsBackportedPerMonthStep(ClosedAsBackportPerMonthTasklet tasklet) {
		return this.stepBuilderFactory.get("closedAsBackportedPerMonthStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step notTriagedPerMonthStep(NotTriagedPerMonthTasklet tasklet) {
		return this.stepBuilderFactory.get("notTriagedPerMonthStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step reportGenerationStep(ReportGeneratingTasklet tasklet) {
		return this.stepBuilderFactory.get("reportGenerationStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step closedAsQuestionPerMonthStep(ClosedAsQuestionPerMonthTasklet tasklet) {
		return this.stepBuilderFactory.get("closedAsQuestionPerMonthStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step monthlyDownloadsStep(MonthlyDownloadCountTasklet tasklet) {
		return this.stepBuilderFactory.get("monthlyDownloadStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Step projectDownloadsStep(ProjectCreationPerMonthTasklet tasklet) {
		return this.stepBuilderFactory.get("projectCreationStep")
				.tasklet(tasklet)
				.build();
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("job")
				.start(teamCreatedPerMonthStep(null))
				.next(communityCreatedPerMonthStep(null))
				.next(closedAsDuplicatePerMonthStep(null))
				.next(closedAsInvalidPerMonthStep(null))
				.next(closedAsEnhancementPerMonthStep(null))
				.next(closedAsBackportedPerMonthStep(null))
				.next(closedAsBugPerMonthStep(null))
				.next(closedAsTaskPerMonthStep(null))
				.next(closedAsDocumentationPerMonthStep(null))
				.next(notTriagedPerMonthStep(null))
				.next(closedAsQuestionPerMonthStep(null))
				.next(monthlyDownloadsStep(null))
				.next(projectDownloadsStep(null))
				.next(reportGenerationStep(null))
				.build();
	}
}
