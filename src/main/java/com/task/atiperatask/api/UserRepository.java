package com.task.atiperatask.api;

import java.util.List;

record UserRepository(String name, String owner, List<BranchDto> branches) {
    record BranchDto(String name, String sha) {}
}
