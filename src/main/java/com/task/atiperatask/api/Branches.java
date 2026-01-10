package com.task.atiperatask.api;

record Branches(String name, Commit commit) {
    record Commit(String sha){}
}
