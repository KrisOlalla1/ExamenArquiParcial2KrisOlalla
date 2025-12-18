package com.examen.branches_api.controller;

import com.examen.branches_api.dto.BranchHolidayRequest;
import com.examen.branches_api.dto.BranchRequest;
import com.examen.branches_api.dto.BranchResponse;
import com.examen.branches_api.dto.HolidayCheckResponse;
import com.examen.branches_api.dto.PhoneUpdateRequest;
import com.examen.branches_api.model.BranchHoliday;
import com.examen.branches_api.service.BranchService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestionar sucursales y sus feriados.
 * Base URL: /api/branches_api/v1/branch
 */
@Slf4j
@RestController
@RequestMapping("/api/branches_api/v1/branch")
@AllArgsConstructor
public class BranchController {

    private final BranchService branchService;

    /**
     * Endpoint 1: Obtener todas las sucursales
     * GET /api/branches_api/v1/branch
     */
    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAllBranches() {
        log.info("API: GET /api/branches_api/v1/branch - Fetching all branches");
        List<BranchResponse> branches = this.branchService.getAllBranches();
        log.info("API: Returning {} branches", branches.size());
        return ResponseEntity.ok(branches);
    }

    /**
     * Endpoint 2: Crear una nueva sucursal (sin feriados)
     * POST /api/branches_api/v1/branch
     */
    @PostMapping
    public ResponseEntity<BranchResponse> createBranch(
            @Valid @RequestBody BranchRequest request) {
        log.info("API: POST /api/branches_api/v1/branch - Creating new branch: {}", request.getName());
        BranchResponse response = this.branchService.createBranch(request);
        log.info("API: Branch created with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint 3: Obtener sucursal por ID
     * GET /api/branches_api/v1/branch/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> getBranchById(@PathVariable String id) {
        log.info("API: GET /api/branches_api/v1/branch/{} - Fetching branch", id);
        BranchResponse response = this.branchService.getBranchById(id);
        log.info("API: Branch found: {}", response.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint 4: Modificar teléfono de sucursal
     * PATCH /api/branches_api/v1/branch/{id}/phone
     */
    @PatchMapping("/{id}/phone")
    public ResponseEntity<BranchResponse> updatePhoneNumber(
            @PathVariable String id,
            @Valid @RequestBody PhoneUpdateRequest request) {
        log.info("API: PATCH /api/branches_api/v1/branch/{}/phone - Updating phone number", id);
        BranchResponse response = this.branchService.updatePhoneNumber(id, request.getPhoneNumber());
        log.info("API: Phone number updated for branch: {}", response.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint 5: Crear feriados para una sucursal
     * POST /api/branches_api/v1/branch/{id}/holiday
     */
    @PostMapping("/{id}/holiday")
    public ResponseEntity<BranchResponse> addHolidays(
            @PathVariable String id,
            @Valid @RequestBody List<BranchHolidayRequest> holidays) {
        log.info("API: POST /api/branches_api/v1/branch/{}/holiday - Adding {} holidays", id, holidays.size());
        BranchResponse response = this.branchService.addHolidays(id, holidays);
        log.info("API: Holidays added to branch: {}", response.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint 6: Eliminar feriado de una sucursal
     * DELETE /api/branches_api/v1/branch/{id}/holiday/{date}
     */
    @DeleteMapping("/{id}/holiday/{date}")
    public ResponseEntity<BranchResponse> deleteHoliday(
            @PathVariable String id,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("API: DELETE /api/branches_api/v1/branch/{}/holiday/{} - Deleting holiday", id, date);
        BranchResponse response = this.branchService.deleteHoliday(id, date);
        log.info("API: Holiday deleted from branch: {}", response.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint 7: Obtener todos los feriados de una sucursal
     * GET /api/branches_api/v1/branch/{id}/holiday
     */
    @GetMapping("/{id}/holiday")
    public ResponseEntity<List<BranchHoliday>> getHolidays(@PathVariable String id) {
        log.info("API: GET /api/branches_api/v1/branch/{}/holiday - Fetching holidays", id);
        List<BranchHoliday> holidays = this.branchService.getHolidays(id);
        log.info("API: Returning {} holidays", holidays.size());
        return ResponseEntity.ok(holidays);
    }

    /**
     * Endpoint 8: Verificar si una fecha es feriado
     * GET /api/branches_api/v1/branch/{id}/holiday/check?date=YYYY-MM-DD
     */
    @GetMapping("/{id}/holiday/check")
    public ResponseEntity<HolidayCheckResponse> isHoliday(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("API: GET /api/branches_api/v1/branch/{}/holiday/check?date={} - Checking if holiday", id, date);
        HolidayCheckResponse response = this.branchService.isHoliday(id, date);
        log.info("API: Date {} is {} for branch {}", date, response.isHoliday() ? "a holiday" : "not a holiday", id);
        return ResponseEntity.ok(response);
    }
}
