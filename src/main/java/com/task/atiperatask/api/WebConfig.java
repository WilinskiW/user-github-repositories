package com.task.atiperatask.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class WebConfig {
    @Bean
    RestClient restClient(@Value("${api.github.url}") String apiUrl) {
        return RestClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("accept", "application/vnd.github+json")
                .build();
    }
}
