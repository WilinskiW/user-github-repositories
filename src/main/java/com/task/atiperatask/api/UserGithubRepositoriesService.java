package com.task.atiperatask.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;


@Service
class UserGithubRepositoriesService {
    private final RestClient restClient;

    UserGithubRepositoriesService(RestClient restClient) {
        this.restClient = restClient;
    }

    ResponseEntity<List<RepositoryResponse>> getUserRepositories(String username) {
        List<GithubRepository> response = restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve().body(new ParameterizedTypeReference<>() {});


        List<RepositoryResponse> repositories = response.stream()
                .filter(repository -> !repository.fork())
                .map(this::mapToRepository)
                .toList();

        return new ResponseEntity<>(repositories, HttpStatus.OK);
    }

    private RepositoryResponse mapToRepository(GithubRepository repository) {
        System.out.println(repository);
        String cleanBranchUrl = repository.branchesUrl().replace("{/branch}", "");

        List<Branches> branches = restClient.get()
                .uri(cleanBranchUrl)
                .retrieve().body(new ParameterizedTypeReference<>() {
                });

        List<RepositoryResponse.BranchDto> branchesMap = branches.stream()
                .map(branch -> new RepositoryResponse.BranchDto(branch.name(), branch.commit().sha()))
                .toList();

        return new RepositoryResponse(
                repository.name(),
                repository.owner().login(),
                branchesMap
        );
    }
}
