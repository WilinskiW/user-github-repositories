package com.task.atiperatask.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class GithubRepositoriesService {
    private final RestClient restClient;

    public GithubRepositoriesService() {
        this.restClient = RestClient.create();
    }

    public ResponseEntity<List<Map<String, Object>>> getUserRepositories(String username) {
        List<UserRepository> response = restClient.get()
                .uri("https://api.github.com/users/" + username + "/repos")
                .retrieve().body(new ParameterizedTypeReference<>() {});

        List<Map<String, Object>> repositories = response.stream()
                .filter(repository -> !repository.fork())
                .map(this::mapToRepository)
                .collect(Collectors.toList());

        return repositories.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(repositories, HttpStatus.OK);
    }

    private Map<String, Object> mapToRepository(UserRepository repository) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", repository.name());
        map.put("owner", repository.owner().login());

        List<Branches> branches = restClient.get()
                .uri(repository.branches_url().replace("{/branch}", ""))
                .retrieve().body(new ParameterizedTypeReference<>() {});

        List<Map<String, String>> branchesMap = new ArrayList<>();
        branches.forEach(branch -> {
            Map<String, String> branchMap = new HashMap<>();
            branchMap.put("name", branch.name());
            branchMap.put("sha", branch.commit().sha());
            branchesMap.add(branchMap);
        });

        map.put("branches", branchesMap);

        return map;
    }

}
