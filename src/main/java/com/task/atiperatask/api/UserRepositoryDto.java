package com.task.atiperatask.api;

import java.util.List;

public record UserRepositoryDto(String name, String owner, List<Branch> branches) {
    public record Branch(String name, String sha) {}
}
