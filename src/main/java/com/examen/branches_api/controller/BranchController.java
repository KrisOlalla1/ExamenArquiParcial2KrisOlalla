package com.examen.branches_api.controller;

import com.examen.branches_api.dto.BranchHolidayRequest;
import com.examen.branches_api.dto.BranchRequest;
import com.examen.branches_api.dto.BranchResponse;
import com.examen.branches_api.dto.HolidayCheckResponse;
import com.examen.branches_api.dto.PhoneUpdateRequest;
import com.examen.branches_api.model.BranchHoliday;
import com.examen.branches_api.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/branches_api/v1/branch")
@AllArgsConstructor
@Tag(name = "Branch Management", description = "API para gestionar sucursales y sus feriados")
public class BranchController {

    private final BranchService branchService;

    @Operation(summary = "Obtener todas las sucursales", description = "Retorna un listado de todas las sucursales registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAllBranches() {
        log.info("API: GET /api/branches_api/v1/branch - Fetching all branches");
        List<BranchResponse> branches = this.branchService.getAllBranches();
        log.info("API: Returning {} branches", branches.size());
        return ResponseEntity.ok(branches);
    }

    @Operation(summary = "Crear una nueva sucursal", description = "Crea una nueva sucursal sin feriados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<BranchResponse> createBranch(
            @Valid @RequestBody BranchRequest request) {
        log.info("API: POST /api/branches_api/v1/branch - Creating new branch: {}", request.getName());
        BranchResponse response = this.branchService.createBranch(request);
        log.info("API: Branch created with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener sucursal por ID", description = "Retorna una sucursal específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> getBranchById(
            @Parameter(description = "ID de la sucursal") @PathVariable String id) {
        log.info("API: GET /api/branches_api/v1/branch/{} - Fetching branch", id);
        BranchResponse response = this.branchService.getBranchById(id);
        log.info("API: Branch found: {}", response.getName());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Modificar teléfono de sucursal", description = "Actualiza el número de teléfono de una sucursal y su fecha de modificación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teléfono actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada"),
            @ApiResponse(responseCode = "400", description = "Número de teléfono inválido")
    })
    @PatchMapping("/{id}/phone")
    public ResponseEntity<BranchResponse> updatePhoneNumber(
            @Parameter(description = "ID de la sucursal") @PathVariable String id,
            @Valid @RequestBody PhoneUpdateRequest request) {
        log.info("API: PATCH /api/branches_api/v1/branch/{}/phone - Updating phone number", id);
        BranchResponse response = this.branchService.updatePhoneNumber(id, request.getPhoneNumber());
        log.info("API: Phone number updated for branch: {}", response.getName());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear feriados para una sucursal", description = "Agrega uno o más feriados a una sucursal existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Feriados creados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de feriados inválidos")
    })
    @PostMapping("/{id}/holiday")
    public ResponseEntity<BranchResponse> addHolidays(
            @Parameter(description = "ID de la sucursal") @PathVariable String id,
            @Valid @RequestBody List<BranchHolidayRequest> holidays) {
        log.info("API: POST /api/branches_api/v1/branch/{}/holiday - Adding {} holidays", id, holidays.size());
        BranchResponse response = this.branchService.addHolidays(id, holidays);
        log.info("API: Holidays added to branch: {}", response.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar feriado de una sucursal", description = "Elimina un feriado específico por su fecha de una sucursal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feriado eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sucursal o feriado no encontrado")
    })
    @DeleteMapping("/{id}/holiday/{date}")
    public ResponseEntity<BranchResponse> deleteHoliday(
            @Parameter(description = "ID de la sucursal") @PathVariable String id,
            @Parameter(description = "Fecha del feriado (formato: YYYY-MM-DD)") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("API: DELETE /api/branches_api/v1/branch/{}/holiday/{} - Deleting holiday", id, date);
        BranchResponse response = this.branchService.deleteHoliday(id, date);
        log.info("API: Holiday deleted from branch: {}", response.getName());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener todos los feriados de una sucursal", description = "Retorna la lista de feriados de una sucursal específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de feriados obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @GetMapping("/{id}/holiday")
    public ResponseEntity<List<BranchHoliday>> getHolidays(
            @Parameter(description = "ID de la sucursal") @PathVariable String id) {
        log.info("API: GET /api/branches_api/v1/branch/{}/holiday - Fetching holidays", id);
        List<BranchHoliday> holidays = this.branchService.getHolidays(id);
        log.info("API: Returning {} holidays", holidays.size());
        return ResponseEntity.ok(holidays);
    }

    @Operation(summary = "Verificar si una fecha es feriado", description = "Verifica si una fecha específica es o no un feriado en una sucursal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación completada"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @GetMapping("/{id}/holiday/check")
    public ResponseEntity<HolidayCheckResponse> isHoliday(
            @Parameter(description = "ID de la sucursal") @PathVariable String id,
            @Parameter(description = "Fecha a verificar (formato: YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("API: GET /api/branches_api/v1/branch/{}/holiday/check?date={} - Checking if holiday", id, date);
        HolidayCheckResponse response = this.branchService.isHoliday(id, date);
        log.info("API: Date {} is {} for branch {}", date, response.isHoliday() ? "a holiday" : "not a holiday", id);
        return ResponseEntity.ok(response);
    }
}
