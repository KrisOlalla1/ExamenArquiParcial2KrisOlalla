package com.examen.branches_api.mapper;

import com.examen.branches_api.dto.BranchRequest;
import com.examen.branches_api.dto.BranchResponse;
import com.examen.branches_api.model.Branch;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class BranchMapperImpl implements BranchMapper {

    @Override
    public BranchResponse toResponse(Branch branch) {
        BranchResponse response = new BranchResponse();
        response.setId(branch.getId());
        response.setName(branch.getName());
        response.setEmailAddress(branch.getEmailAddress());
        response.setPhoneNumber(branch.getPhoneNumber());
        response.setState(branch.getState());
        response.setCreationDate(branch.getCreationDate());
        response.setLastModifiedDate(branch.getLastModifiedDate());
        response.setBranchHolidays(branch.getBranchHolidays());
        return response;
    }

    @Override
    public Branch toEntity(BranchRequest request) {
        Branch branch = new Branch();
        branch.setName(request.getName());
        branch.setEmailAddress(request.getEmailAddress());
        branch.setPhoneNumber(request.getPhoneNumber());
        branch.setState("ACTIVE");
        branch.setCreationDate(LocalDateTime.now());
        branch.setLastModifiedDate(LocalDateTime.now());
        branch.setBranchHolidays(new ArrayList<>());
        return branch;
    }
}
