package com.task.atiperatask.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
class UserRepositoriesController {
    private final UserGithubRepositoriesService repositoryService;

    UserRepositoriesController(UserGithubRepositoriesService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping("/{username}/repositories")
    ResponseEntity<List<RepositoryResponse>> getUserRepositories(@PathVariable String username) {
        return repositoryService.getUserRepositories(username);
    }
}
