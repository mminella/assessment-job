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

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * @author Michael Minella
 */
@Configuration
public class GithubConfiguration {

	ProjectAssessmentProperties properties;

	public GithubConfiguration(ProjectAssessmentProperties properties) {
		this.properties = properties;
	}

	@Bean
	public RestOperations restTemplate() {
		RestTemplate restTemplate = new GithubRateLimitRestTemplate();
		restTemplate.setInterceptors(Collections.singletonList(new BasicAuthorizationInterceptor(this.properties.getGithubUser(), this.properties.getGithubToken())));
		return restTemplate;
	}

	private static class BasicAuthorizationInterceptor implements ClientHttpRequestInterceptor {

		private static final Charset UTF_8 = StandardCharsets.UTF_8;

		private final String username;

		private final String password;

		BasicAuthorizationInterceptor(String username, String password) {
			this.username = username;
			this.password = (password != null) ? password : "";
		}

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
				throws IOException {
			String token = Base64Utils.encodeToString((this.username + ":" + this.password).getBytes(UTF_8));
			request.getHeaders().add("Authorization", "Basic " + token);
			return execution.execute(request, body);
		}

	}

	/**
	 * Custom RestTemplate to handle GitHub rate limiting See
	 * https://docs.github.com/en/free-pro-team@latest/rest/overview/resources-in-the-rest-api#rate-limiting
	 *
	 */
	private static class GithubRateLimitRestTemplate extends RestTemplate {
		/**
		 * Account for any clock skew between the client and GitHub Server APIs. Adjust this as you want, but 30 seconds
		 * seems to work pretty well.
		 */
		private static final Duration RATE_LIMIT_PADDING = Duration.ofSeconds(30);

		/**
		 * The count of requests that can be made before requests are blocked due to rate limitting. Comes from
		 * X-RateLimit-Remaining response header
		 */
		private long rateLimitRemaining = 5000;

		/**
		 * The time at which the current rate limit window resets in <a href="http://en.wikipedia.org/wiki/Unix_time">UTC
		 * epoch seconds</a>. X-RateLimit-Reset	 response header.
		 */
		private long rateLimitRestInSeconds = 0L;

		@Override
		protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
			if (this.rateLimitRemaining <= 1) {
				long nowInMs = System.currentTimeMillis();
				long rateLimitResetInMs = this.rateLimitRestInSeconds * 1000 + RATE_LIMIT_PADDING.toMillis();

				long msToReset = rateLimitResetInMs - nowInMs;
				if (msToReset > 0) {
					try {
						System.out.println("Rate limit remaining reached " + this.rateLimitRemaining + ". Sleeping for " + msToReset + " milliseconds. Will resume at " + new Date(rateLimitResetInMs));
						Thread.sleep(msToReset);
					} catch (InterruptedException e) {
						throw new RuntimeException("Failed to sleep for rate limit", e);
					}
				}
			}
			return super.doExecute(url, method, requestCallback, responseExtractor);
		}

		@Override
		protected void handleResponse(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
			String rateLimitRemainingHeader = response.getHeaders().getFirst("X-RateLimit-Remaining");
			if (rateLimitRemainingHeader != null) {
				this.rateLimitRemaining = Long.parseLong(rateLimitRemainingHeader);
			}
			String rateLimitRestHeader = response.getHeaders().getFirst("X-RateLimit-Reset");
			if (rateLimitRestHeader != null) {
				this.rateLimitRestInSeconds = Long.parseLong(rateLimitRestHeader);
			}
			super.handleResponse(url, method, response);
		}
	}
}
