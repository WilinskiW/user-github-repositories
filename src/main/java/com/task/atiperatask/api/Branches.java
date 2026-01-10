package com.task.atiperatask.api;

public record Branches(String name, Commit commit) {
    public record Commit(String sha){}
}
