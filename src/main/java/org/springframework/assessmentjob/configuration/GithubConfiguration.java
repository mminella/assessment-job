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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * @author Michael Minella
 */
@Configuration
public class GithubConfiguration {

	@Bean
	public RestOperations restTemplate(@Value("${github.user}") String username, @Value("${github.token}")String token) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.singletonList(new BasicAuthorizationInterceptor(username, token)));
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
}
