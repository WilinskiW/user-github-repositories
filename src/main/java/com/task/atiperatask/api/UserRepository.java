package com.task.atiperatask.api;

record UserRepository(String name, Owner owner, boolean fork, String branches_url) {
    record Owner(String login) {
    }
}
