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

import org.springframework.assessmentjob.configuration.ProjectAssessmentProperties;
import org.springframework.assessmentjob.util.ReportKey;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.Map;

/**
 * @author Michael Minella
 */
@Component public class ClosedAsBackportPerMonthTasklet extends BaseGithubSearchTasklet {

    public ClosedAsBackportPerMonthTasklet(RestOperations restTemplate, Map<ReportKey, List<Long>> report,
        ProjectAssessmentProperties properties) {
        super(restTemplate, report, properties);
    }

    @Override public String getQuery() {
        return properties.getProjectRepo() + " is:issue closed:%s is:closed label:\"has: backports\"";
    }

    @Override public String getResultKey() {
        return "total_count";
    }

    @Override public ReportKey getReportKey() {
        return ReportKey.CLOSED_AS_BACKPORT;
    }
}
