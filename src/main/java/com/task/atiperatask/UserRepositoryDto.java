package com.task.atiperatask;

import java.util.List;

record UserRepositoryDto(String name, String owner, List<Branch> branches) {
    record Branch(String name, String sha) {}
}
