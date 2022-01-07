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

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import org.springframework.assessmentjob.configuration.ProjectAssessmentProperties;
import org.springframework.assessmentjob.util.ReportKey;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * @author Michael Minella
 */
@Component
public class MonthlyDownloadCountTasklet extends ReportTasklet {

	private static final Period MONTHLY = Period.ofMonths(1);

	private final RestHighLevelClient client;

	public MonthlyDownloadCountTasklet(Map<ReportKey, List<Long>> report,
			RestHighLevelClient client,
			ProjectAssessmentProperties properties) {
		super(report, properties);
		this.client = client;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		List<Long> values = new ArrayList<>();

		LocalDate curDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);

		for (int i = 0; i < MONTHS; i++) {
			values.add(getDownloadCount(properties.getProjectId(),
					properties.getProjectArtifactId(),
					curDate,
					MONTHLY));

			curDate = curDate.minusMonths(1);
		}

		report.put(getReportKey(), values);

		return RepeatStatus.FINISHED;
	}

	public long getDownloadCount(String groupId, String artifactId, LocalDate start, Period period) throws IOException {
		long startTimestamp = toEpoch(start.withDayOfMonth(1));
		long endTimestamp = toEpoch(start.plus(period));
		SearchRequest searchRequest = new SearchRequest("downloads");
		QueryBuilder query = query(groupId, artifactId, startTimestamp, endTimestamp);
		searchRequest.source(new SearchSourceBuilder().query(query)
				.aggregation(AggregationBuilders.sum("downloads").field("count")));
		SearchResponse response = this.client.search(searchRequest, RequestOptions.DEFAULT);
		ParsedSum downloads = response.getAggregations().get("downloads");
		return (long) downloads.getValue();
	}

	private QueryBuilder query(String groupId, String artifactId, long from, long to) {
		return QueryBuilders.boolQuery().filter(new MatchQueryBuilder("projectId", groupId))
				.filter(new MatchQueryBuilder("artifactId", artifactId))
				.filter(QueryBuilders.rangeQuery("from").gte(from)).filter(QueryBuilders.rangeQuery("to").lt(to));

	}

	private static long toEpoch(LocalDate time) {
		return time.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
	}


	@Override
	public ReportKey getReportKey() {
		return ReportKey.MAVEN_DOWNLOADS;
	}
}
