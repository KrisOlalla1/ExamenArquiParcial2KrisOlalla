package com.examen.branches_api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "branches")
public class Branch {

    @Id
    private String id;

    private String emailAddress;
    private String name;
    private String phoneNumber;
    private String state;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private List<BranchHoliday> branchHolidays;

    public Branch() {
        this.branchHolidays = new ArrayList<>();
        this.state = "ACTIVE";
    }

    public Branch(String id) {
        this();
        this.id = id;
    }
}
