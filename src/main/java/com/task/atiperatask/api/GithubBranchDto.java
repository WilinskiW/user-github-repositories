package com.task.atiperatask.api;

record GithubBranchDto(String name, Commit commit) {
    record Commit(String sha){}
}
