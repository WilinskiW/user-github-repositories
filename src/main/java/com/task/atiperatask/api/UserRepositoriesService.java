package com.task.atiperatask.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class UserRepositoriesService {
    private final RestClient restClient;

    UserRepositoriesService(RestClient restClient) {
        this.restClient = restClient;
    }

    List<UserRepositoryDto> getUserRepositories(String username) {
        List<GithubRepositoryDto> githubRepos = fetchRepositories(username);
        return convertToResponse(githubRepos);
    }

    private List<GithubRepositoryDto> fetchRepositories(String username) {
        return restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    private List<UserRepositoryDto> convertToResponse(List<GithubRepositoryDto> githubRepos) {
        return Optional.ofNullable(githubRepos)
                .orElse(Collections.emptyList())
                .stream()
                .filter(repository -> !repository.fork())
                .map(this::mapToUserRepositoryDto)
                .toList();
    }


    private UserRepositoryDto mapToUserRepositoryDto(GithubRepositoryDto githubRepo) {
        String cleanBranchUrl = githubRepo.branchesUrl().replace("{/branch}", "");

        List<GithubBranchDto> githubRepoBranches = fetchBranches(cleanBranchUrl);

        List<UserRepositoryDto.Branch> repoBranches = convertToBranches(githubRepoBranches);

        return new UserRepositoryDto(
                githubRepo.name(),
                githubRepo.owner().login(),
                repoBranches
        );
    }

    private List<GithubBranchDto> fetchBranches(String cleanBranchUrl) {
        return restClient.get()
                .uri(cleanBranchUrl)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    private List<UserRepositoryDto.Branch> convertToBranches(List<GithubBranchDto> githubRepoBranches) {
        return Optional.ofNullable(githubRepoBranches)
                .orElse(Collections.emptyList())
                .stream()
                .map(githubBranchDto ->
                        new UserRepositoryDto.Branch(githubBranchDto.name(), githubBranchDto.commit().sha())
                )
                .toList();
    }
}
