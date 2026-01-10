package com.task.atiperatask.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GithubController {
    private final GithubRepositoriesService repositoryService;

    public GithubController(GithubRepositoriesService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @PostMapping("/repositories")
    public ResponseEntity<List<Map<String, Object>>> getUserRepositories(@RequestBody Map<String, String> username) {
        return repositoryService.getUserRepositories(username.get("username").strip());
    }
}
