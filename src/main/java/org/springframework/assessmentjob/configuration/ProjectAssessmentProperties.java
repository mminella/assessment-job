/*
 * Copyright 2022-2022 the original author or authors.
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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Michael Minella
 */
@ConfigurationProperties(prefix = "spring.projectassessment")
public class ProjectAssessmentProperties {

	private String githubUser;
	private String githubToken;
	private String[] projectUsers;
	private String projectRepo;
	private String projectId;
	private String projectArtifactId;
	private String dependency;
	private boolean outputDates;

	public boolean isOutputDates() {
		return outputDates;
	}

	public void setOutputDates(boolean outputDates) {
		this.outputDates = outputDates;
	}

	public String getDependency() {
		return dependency;
	}

	public void setDependency(String dependency) {
		this.dependency = dependency;
	}

	public String getGithubUser() {
		return githubUser;
	}

	public void setGithubUser(String githubUser) {
		this.githubUser = githubUser;
	}

	public String getGithubToken() {
		return githubToken;
	}

	public void setGithubToken(String githubToken) {
		this.githubToken = githubToken;
	}

	public String[] getProjectUsers() {
		return projectUsers;
	}

	public void setProjectUsers(String[] projectUsers) {
		this.projectUsers = projectUsers;
	}

	public String getProjectRepo() {
		return projectRepo;
	}

	public void setProjectRepo(String projectRepo) {
		this.projectRepo = projectRepo;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectArtifactId() {
		return projectArtifactId;
	}

	public void setProjectArtifactId(String projectArtifactId) {
		this.projectArtifactId = projectArtifactId;
	}
}
