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

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.assessmentjob.configuration.ProjectAssessmentProperties;
import org.springframework.assessmentjob.util.ReportKey;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Minella
 */
@Component public class ReportGeneratingTasklet implements Tasklet {

    private final Map<ReportKey, List<Long>> report;
    private final ProjectAssessmentProperties properties;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public ReportGeneratingTasklet(Map<ReportKey, List<Long>> report, ProjectAssessmentProperties properties) {
        this.report = report;
        this.properties = properties;
    }

    @Override public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        System.out.println(">> GENERATING REPORT");
        System.out.println(">> Report size = " + report.size());

        for (ReportKey key : ReportKey.values()) {
            Calendar c = Calendar.getInstance();   // this takes current date
            c.add(Calendar.MONTH, -12);
            c.set(Calendar.DAY_OF_MONTH, 1);
            Date startDate = c.getTime();
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = c.getTime();

            List<Long> values = report.get(key);

            if (values != null) {
                System.out.println(key.getValue());

                for (int i = values.size() - 1; i >= 0; i--) {

                    if (this.properties.isOutputDates()) {
                        System.out.println(String.format("\t%s: %s", getDateString(startDate, endDate), values.get(i)));
                    } else {
                        System.out.println(values.get(i));
                    }

                    startDate = DateUtils.addMonths(startDate, 1);
                    endDate = DateUtils.addMonths(endDate, 1);
                    Calendar instance = Calendar.getInstance();
                    instance.setTime(endDate);
                    instance.set(Calendar.DAY_OF_MONTH, instance.getActualMaximum(Calendar.DAY_OF_MONTH));
                    endDate = instance.getTime();
                }
            }
        }

        return RepeatStatus.FINISHED;
    }

    private String getDateString(Date startDate, Date endDate) {

        return String.format("%s - %s", formatter.format(startDate), formatter.format(endDate));
    }
}
