package com.examen.branches_api.mapper;

import com.examen.branches_api.dto.BranchRequest;
import com.examen.branches_api.dto.BranchResponse;
import com.examen.branches_api.model.Branch;

public interface BranchMapper {

    BranchResponse toResponse(Branch branch);

    Branch toEntity(BranchRequest request);
}
