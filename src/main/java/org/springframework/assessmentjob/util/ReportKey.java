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

package org.springframework.assessmentjob.util;

/**
 * @author Michael Minella
 */
public enum ReportKey {

	TEAM_CREATED("Team created per month"),
	COMMUNITY_CREATED("Community created per month"),
	CLOSED_AS_DUPE("Closed as duplicate (or equivelant) per month"),
	CLOSED_AS_QUESTION("Closed as a question (StackOverflow/etc) per month"),
	CLOSED_AS_DECLINED("Closed as declined/invalid or equivalent per month"),
	CLOSED_AS_ENHANCEMENT("Closed as an enhancement per month"),
	CLOSED_AS_BACKPORT("Closed to be backported/forward-port per month"),
	CLOSED_AS_REGRESSION("Closed as regression or bug per month"),
	CLOSED_AS_TASK("Closed as task/dependency upgrade/etc per month"),
	CLOSED_AS_DOCS("Closed as documentation/site related issue per month"),
	ISSUE_WITH_MILESTONE("Issues with a milestone assigned per month"),
	ISSUE_WITH_BACKLOG("Issues with a non-specific milestone assigned (backlog, etc)"),
	ISSUE_BLOCKED("Issues identified as blocked/on hold/etc per month"),
	ISSUE_PENDING_DESIGN("Issues pending a design session per month"),
	ISSUE_NO_COMMENT("Issues without a comment from a team member per month"),
	PROJECT_STARTS("Project creations from start.spring.io per month"),
	MAVEN_DOWNLOADS("Project downloads from Maven Central per month");

	private String value;

	ReportKey(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
