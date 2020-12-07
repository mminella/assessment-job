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

package org.springframework.assessmentjob.batch;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

/**
 * @author Michael Minella
 */
@Component
public class ClosedAsDocumentationPerMonthTasklet extends BaseGithubSearchTasklet {

	public ClosedAsDocumentationPerMonthTasklet(RestOperations restTemplate,
			@Value("${spring.project.repo}") String repo,
			Map<String, List<Integer>> report) {
		super(restTemplate, repo, report);
	}

	@Override
	public String getQuery() {
		return repo + " is:issue closed:%s is:closed label:\"in: documentation\"";
	}

	@Override
	public String getResultKey() {
		return "total_count";
	}

	@Override
	public String getReportKey() {
		return "closed_as_documentation";
	}
}