package com.task.atiperatask.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
class UserRepositoriesController {
    private final UserRepositoriesService repositoryService;

    UserRepositoriesController(UserRepositoriesService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping("/{username}/repositories")
    ResponseEntity<List<UserRepositoryDto>> getUserRepositories(@PathVariable String username) {
        List<UserRepositoryDto> repositories = repositoryService.getUserRepositories(username);
        return new ResponseEntity<>(repositories, HttpStatus.OK);
    }
}
