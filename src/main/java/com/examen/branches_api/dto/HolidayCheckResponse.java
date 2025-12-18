package com.examen.branches_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HolidayCheckResponse {

    private String branchId;
    private LocalDate date;
    private boolean isHoliday;
    private String holidayName;
}
