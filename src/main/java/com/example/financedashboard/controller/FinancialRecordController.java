package com.example.financedashboard.controller;

import com.example.financedashboard.dto.DashboardSummaryDTO;
import com.example.financedashboard.dto.FinancialRecordDTO;
import com.example.financedashboard.security.CurrentUser;
import com.example.financedashboard.security.UserPrincipal;
import com.example.financedashboard.service.FinancialRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
@Tag(name = "Financial Records", description = "Financial record management APIs")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class FinancialRecordController {

    private final FinancialRecordService recordService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get current user's financial records")
    public ResponseEntity<Page<FinancialRecordDTO>> getUserRecords(
            @CurrentUser UserPrincipal currentUser,
            @PageableDefault(size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        if (currentUser == null) {
            log.error("CurrentUser is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(recordService.getUserRecords(currentUser.getId(), pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get financial record by ID")
    public ResponseEntity<FinancialRecordDTO> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(recordService.getRecordById(id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create new financial record")
    public ResponseEntity<FinancialRecordDTO> createRecord(
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody FinancialRecordDTO recordDTO) {

        if (currentUser == null) {
            log.error("CurrentUser is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Creating record for user: {}", currentUser.getEmail());
        return new ResponseEntity<>(recordService.createRecord(currentUser.getId(), recordDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update financial record")
    public ResponseEntity<FinancialRecordDTO> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody FinancialRecordDTO recordDTO) {
        return ResponseEntity.ok(recordService.updateRecord(id, recordDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete financial record")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get dashboard summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary(
            @CurrentUser UserPrincipal currentUser,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        if (currentUser == null) {
            log.error("CurrentUser is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(recordService.getDashboardSummary(currentUser.getId(), startDate, endDate));
    }
}