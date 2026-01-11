package com.task.atiperatask.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
class UserGithubRepositoriesService {
    private final RestClient restClient;

    UserGithubRepositoriesService(RestClient restClient) {
        this.restClient = restClient;
    }

    List<UserRepository> getUserRepositories(String username) {
        List<GithubRepository> githubRepos = restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});


        return Optional.ofNullable(githubRepos)
                .orElse(Collections.emptyList())
                .stream()
                .filter(repository -> !repository.fork())
                .map(this::mapToUserRepository)
                .toList();
    }

    private UserRepository mapToUserRepository(GithubRepository githubRepo) {
        String cleanBranchUrl = githubRepo.branchesUrl().replace("{/branch}", "");

        List<GithubBranch> githubRepoBranches = restClient.get()
                .uri(cleanBranchUrl)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        List<UserRepository.BranchDto> repoBranches = Optional.ofNullable(githubRepoBranches)
                .orElse(Collections.emptyList())
                .stream()
                .map(githubBranch ->
                        new UserRepository.BranchDto(githubBranch.name(), githubBranch.commit().sha())
                )
                .toList();

        return new UserRepository(
                githubRepo.name(),
                githubRepo.owner().login(),
                repoBranches
        );
    }
}
