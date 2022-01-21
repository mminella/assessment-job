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

package org.springframework.assessmentjob.batch;

import org.springframework.assessmentjob.configuration.ProjectAssessmentProperties;
import org.springframework.assessmentjob.util.ReportKey;
import org.springframework.batch.core.step.tasklet.Tasklet;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Minella
 */
public abstract class ReportTasklet implements Tasklet {
    public static final int MONTHS = 13;
    protected final Map<ReportKey, List<Long>> report;
    protected final ProjectAssessmentProperties properties;
    protected SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public ReportTasklet(Map<ReportKey, List<Long>> report, ProjectAssessmentProperties properties) {
        this.report = report;
        this.properties = properties;
    }

    public abstract ReportKey getReportKey();
}
