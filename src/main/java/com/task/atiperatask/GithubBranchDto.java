package com.task.atiperatask;

record GithubBranchDto(String name, Commit commit) {
    record Commit(String sha){}
}
