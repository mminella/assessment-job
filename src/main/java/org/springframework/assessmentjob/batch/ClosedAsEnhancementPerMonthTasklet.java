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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.assessmentjob.configuration.ProjectAssessmentProperties;
import org.springframework.assessmentjob.util.DateCalculationUtils;
import org.springframework.assessmentjob.util.RateLimits;
import org.springframework.assessmentjob.util.ReportKey;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Minella
 */
@Component public class ClosedAsEnhancementPerMonthTasklet extends ReportTasklet {

    private final RestOperations restTemplate;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public ClosedAsEnhancementPerMonthTasklet(RestOperations restTemplate, Map<ReportKey, List<Long>> report,
        ProjectAssessmentProperties properties) {
        super(report, properties);
        this.restTemplate = restTemplate;
    }

    @Override public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        String featureQuery =
            this.properties.getProjectRepo() + " is:issue closed:%s is:closed label:\"type: feature\"";
        String enhancementQuery =
            this.properties.getProjectRepo() + " is:issue closed:%s is:closed label:\"type: enhancement\"";
        long issueCount = 0;

        // For each month
        Date startDate = DateCalculationUtils.getFirstMonthStartDate();
        Date endDate = DateCalculationUtils.getFirstMonthEndDate();

        List<Long> values = new ArrayList<>(MONTHS);

        for (int i = 0; i < MONTHS; i++) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.github.com/search/issues")
                .queryParam("q", String.format(featureQuery, getDateString(startDate, endDate)));

            URI uri = builder.build().encode().toUri();
            System.out.println(">> " + uri.toString());
            HttpEntity<String> response = this.restTemplate.getForEntity(uri, String.class);

            RateLimits.longDelay();

            Map<String, Object> result = new ObjectMapper().readValue(response.getBody(), HashMap.class);
            issueCount += (Integer) result.get("total_count");

            builder = UriComponentsBuilder.fromHttpUrl("https://api.github.com/search/issues")
                .queryParam("q", String.format(enhancementQuery, getDateString(startDate, endDate)));

            uri = builder.build().encode().toUri();
            System.out.println(">> " + uri.toString());
            response = this.restTemplate.getForEntity(uri, String.class);

            result = new ObjectMapper().readValue(response.getBody(), HashMap.class);
            issueCount += ((Integer) result.get("total_count")).longValue();

            values.add(issueCount);

            startDate = DateUtils.addMonths(startDate, -1);
            endDate = DateUtils.addMonths(endDate, -1);
            Calendar instance = Calendar.getInstance();
            instance.setTime(endDate);
            instance.set(Calendar.DAY_OF_MONTH, instance.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate = instance.getTime();

            issueCount = 0;
        }

        report.put(getReportKey(), values);

        return RepeatStatus.FINISHED;
    }

    private String getDateString(Date startDate, Date endDate) {

        return String.format("%s..%s", formatter.format(startDate), formatter.format(endDate));
    }

    @Override public ReportKey getReportKey() {
        return ReportKey.CLOSED_AS_ENHANCEMENT;
    }
}
