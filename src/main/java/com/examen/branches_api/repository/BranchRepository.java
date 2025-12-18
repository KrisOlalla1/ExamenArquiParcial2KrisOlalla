package com.examen.branches_api.repository;

import com.examen.branches_api.model.Branch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends MongoRepository<Branch, String> {
}
