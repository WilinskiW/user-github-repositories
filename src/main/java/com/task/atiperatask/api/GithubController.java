package com.task.atiperatask.api;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class GithubController {
    private final GithubRepositoriesService repositoryService;

    public GithubController(GithubRepositoriesService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @PostMapping("/repositories")
    public void getUserRepositories(@RequestBody Map<String, String> body) {
        String authToken = body.get("authToken");
        repositoryService.getUserRepositories(authToken);
    }
}
