package com.examen.branches_api.service;

import com.examen.branches_api.dto.BranchHolidayRequest;
import com.examen.branches_api.dto.BranchRequest;
import com.examen.branches_api.dto.BranchResponse;
import com.examen.branches_api.dto.HolidayCheckResponse;
import com.examen.branches_api.exception.BranchNotFoundException;
import com.examen.branches_api.exception.HolidayNotFoundException;
import com.examen.branches_api.model.Branch;
import com.examen.branches_api.model.BranchHoliday;
import com.examen.branches_api.repository.BranchRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    @Transactional(readOnly = true)
    public List<BranchResponse> getAllBranches() {
        log.info("Fetching all branches");
        List<Branch> branches = this.branchRepository.findAll();
        log.info("Found {} branches", branches.size());
        return branches.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BranchResponse createBranch(BranchRequest request) {
        log.info("Creating new branch with name: {}", request.getName());

        Branch branch = new Branch();
        branch.setName(request.getName());
        branch.setEmailAddress(request.getEmailAddress());
        branch.setPhoneNumber(request.getPhoneNumber());
        branch.setState("ACTIVE");
        branch.setCreationDate(LocalDateTime.now());
        branch.setLastModifiedDate(LocalDateTime.now());
        branch.setBranchHolidays(new ArrayList<>());

        Branch savedBranch = this.branchRepository.save(branch);
        log.info("Branch created successfully with ID: {}", savedBranch.getId());

        return mapToResponse(savedBranch);
    }

    @Transactional(readOnly = true)
    public BranchResponse getBranchById(String id) {
        log.info("Fetching branch with ID: {}", id);
        Branch branch = this.branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + id));
        log.info("Branch found: {}", branch.getName());
        return mapToResponse(branch);
    }

    @Transactional
    public BranchResponse updatePhoneNumber(String id, String phoneNumber) {
        log.info("Updating phone number for branch ID: {}", id);

        Branch branch = this.branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + id));

        branch.setPhoneNumber(phoneNumber);
        branch.setLastModifiedDate(LocalDateTime.now());

        Branch updatedBranch = this.branchRepository.save(branch);
        log.info("Phone number updated successfully for branch: {}", updatedBranch.getName());

        return mapToResponse(updatedBranch);
    }

    @Transactional
    public BranchResponse addHolidays(String id, List<BranchHolidayRequest> holidayRequests) {
        log.info("Adding {} holidays to branch ID: {}", holidayRequests.size(), id);

        Branch branch = this.branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + id));

        List<BranchHoliday> holidays = holidayRequests.stream()
                .map(req -> new BranchHoliday(req.getDate(), req.getName()))
                .collect(Collectors.toList());

        if (branch.getBranchHolidays() == null) {
            branch.setBranchHolidays(new ArrayList<>());
        }
        branch.getBranchHolidays().addAll(holidays);
        branch.setLastModifiedDate(LocalDateTime.now());

        Branch updatedBranch = this.branchRepository.save(branch);
        log.info("Holidays added successfully to branch: {}", updatedBranch.getName());

        return mapToResponse(updatedBranch);
    }

    @Transactional
    public BranchResponse deleteHoliday(String id, LocalDate date) {
        log.info("Deleting holiday on date {} from branch ID: {}", date, id);

        Branch branch = this.branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + id));

        if (branch.getBranchHolidays() == null || branch.getBranchHolidays().isEmpty()) {
            throw new HolidayNotFoundException("No holidays found for branch ID: " + id);
        }

        boolean removed = branch.getBranchHolidays().removeIf(h -> h.getDate().equals(date));

        if (!removed) {
            throw new HolidayNotFoundException("Holiday not found on date: " + date);
        }

        branch.setLastModifiedDate(LocalDateTime.now());
        Branch updatedBranch = this.branchRepository.save(branch);
        log.info("Holiday deleted successfully from branch: {}", updatedBranch.getName());

        return mapToResponse(updatedBranch);
    }

    @Transactional(readOnly = true)
    public List<BranchHoliday> getHolidays(String id) {
        log.info("Fetching holidays for branch ID: {}", id);

        Branch branch = this.branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + id));

        List<BranchHoliday> holidays = branch.getBranchHolidays() != null
                ? branch.getBranchHolidays()
                : new ArrayList<>();

        log.info("Found {} holidays for branch: {}", holidays.size(), branch.getName());
        return holidays;
    }

    @Transactional(readOnly = true)
    public HolidayCheckResponse isHoliday(String id, LocalDate date) {
        log.info("Checking if {} is a holiday for branch ID: {}", date, id);

        Branch branch = this.branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + id));

        Optional<BranchHoliday> holiday = Optional.empty();
        if (branch.getBranchHolidays() != null) {
            holiday = branch.getBranchHolidays().stream()
                    .filter(h -> h.getDate().equals(date))
                    .findFirst();
        }

        boolean isHoliday = holiday.isPresent();
        String holidayName = holiday.map(BranchHoliday::getName).orElse(null);

        log.info("Date {} is {} for branch: {}", date, isHoliday ? "a holiday (" + holidayName + ")" : "not a holiday",
                branch.getName());

        return new HolidayCheckResponse(id, date, isHoliday, holidayName);
    }

    private BranchResponse mapToResponse(Branch branch) {
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
}
