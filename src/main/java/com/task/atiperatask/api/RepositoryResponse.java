package com.task.atiperatask.api;

import java.util.List;

record RepositoryResponse(String name, String owner, List<BranchDto> branches) {
    record BranchDto(String name, String sha) {}
}
