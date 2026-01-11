package com.task.atiperatask.api;

record GithubBranch(String name, Commit commit) {
    record Commit(String sha){}
}
