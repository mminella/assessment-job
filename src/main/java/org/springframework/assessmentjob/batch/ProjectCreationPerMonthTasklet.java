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

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.assessmentjob.configuration.ProjectAssessmentProperties;
import org.springframework.assessmentjob.util.ReportKey;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Minella
 */
@Component public class ProjectCreationPerMonthTasklet extends ReportTasklet {

    private static final Period MONTHLY = Period.ofMonths(1);

    private final RestHighLevelClient client;

    public ProjectCreationPerMonthTasklet(Map<ReportKey, List<Long>> report, RestHighLevelClient client,
        ProjectAssessmentProperties properties) {
        super(report, properties);
        this.client = client;
    }

    private static long toEpoch(LocalDate time) {
        return time.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    @Override public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<Long> values = new ArrayList<>();

        LocalDate curDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);

        for (int i = 0; i < MONTHS; i++) {
            values.add(getProjectCreationCount(properties.getDependency(), curDate, MONTHLY));

            curDate = curDate.minusMonths(1);
        }

        report.put(getReportKey(), values);

        return RepeatStatus.FINISHED;
    }

    private long getProjectCreationCount(String dependency, LocalDate start, Period period) throws IOException {
        long startTimestamp = toEpoch(start);
        long endTimestamp = toEpoch(start.plus(period));
        CountRequest countRequest = new CountRequest();
        QueryBuilder query = query(dependency, startTimestamp, endTimestamp);
        countRequest.query(query);
        CountResponse count = this.client.count(countRequest, RequestOptions.DEFAULT);
        return count.getCount();
    }

    private QueryBuilder query(String dependency, long from, long to) {
        return QueryBuilders.boolQuery().filter(new MatchQueryBuilder("dependencies.values", dependency))
            .mustNot(new MatchQueryBuilder("errorState.invalid", true))
            .filter(QueryBuilders.rangeQuery("generationTimestamp").gte(from).lt(to));

    }

    @Override public ReportKey getReportKey() {
        return ReportKey.PROJECT_STARTS;
    }
}
