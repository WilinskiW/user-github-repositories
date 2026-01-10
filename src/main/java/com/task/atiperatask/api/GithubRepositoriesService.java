package com.task.atiperatask.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;


@Service
public class GithubRepositoriesService {
    public void getUserRepositories(String authToken) {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://api.github.com/user/repos")
                .defaultHeaders(
                        httpHeaders -> {
                            httpHeaders.add("Authorization", "Bearer " + authToken);
                            httpHeaders.add("Accept", "application/vnd.github+json");
                        }
                )
                .build();

        List<RepositoriesResponse> response = restClient.get().retrieve().body(new ParameterizedTypeReference<>() {
        });
    }
}
