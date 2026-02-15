package com.task.atiperatask;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
class UserRepositoriesController {
    private final UserRepositoriesService repositoryService;

    UserRepositoriesController(UserRepositoriesService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping(value = "/{username}/repositories", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserNonForkRepositoriesDto> getUserRepositories(@PathVariable String username) {
        UserNonForkRepositoriesDto responseBody = repositoryService.getUserRepositories(username);
        return ResponseEntity.ok(responseBody);
    }
}
