package com.task.atiperatask.api;

import com.fasterxml.jackson.annotation.JsonProperty;

record GithubRepositoryDto(String name, Owner owner, boolean fork, @JsonProperty("branches_url") String branchesUrl) {
    record Owner(String login) {
    }
}
