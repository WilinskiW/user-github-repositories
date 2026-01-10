package com.task.atiperatask.api;


public record UserRepository(String name, Owner owner, boolean fork, String branches_url) {
    public record Owner(String login) {
    }
}
