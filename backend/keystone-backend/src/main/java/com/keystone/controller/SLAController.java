package com.keystone.controller;

import com.keystone.dto.SLARequest;
import com.keystone.dto.SLAResponse;
import com.keystone.service.SLAService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slas")
public class SLAController {

    private final SLAService slaService;

    public SLAController(SLAService slaService) {
        this.slaService = slaService;
    }

    // =========================
    // CREATE SLA
    // =========================

    @PostMapping
    public ResponseEntity<SLAResponse> createSLA(
            @Valid @RequestBody SLARequest request) {

        SLAResponse response = slaService.createSLA(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // GET ALL SLAs
    // =========================

    @GetMapping
    public ResponseEntity<List<SLAResponse>> getAllSLAs() {

        return ResponseEntity.ok(
                slaService.getAllSLAs()
        );
    }

    // =========================
    // GET ACTIVE SLAs
    // =========================

    @GetMapping("/active")
    public ResponseEntity<List<SLAResponse>> getActiveSLAs() {

        return ResponseEntity.ok(
                slaService.getActiveSLAs()
        );
    }

    // =========================
    // GET SLA BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<SLAResponse> getSLAById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                slaService.getSLAById(id)
        );
    }

    // =========================
    // UPDATE SLA
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<SLAResponse> updateSLA(
            @PathVariable Long id,
            @Valid @RequestBody SLARequest request) {

        return ResponseEntity.ok(
                slaService.updateSLA(id, request)
        );
    }

    // =========================
    // DELETE SLA
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSLA(
            @PathVariable Long id) {

        slaService.deleteSLA(id);

        return ResponseEntity.noContent().build();
    }
}
