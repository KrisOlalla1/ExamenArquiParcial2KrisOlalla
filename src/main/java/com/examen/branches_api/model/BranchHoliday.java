package com.examen.branches_api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BranchHoliday {

    private LocalDate date;
    private String name;

    public BranchHoliday() {
    }

    public BranchHoliday(LocalDate date, String name) {
        this.date = date;
        this.name = name;
    }
}
