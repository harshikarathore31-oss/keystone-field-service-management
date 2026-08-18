package com.keystone.controller;

import com.keystone.dto.PartRequest;
import com.keystone.dto.PartResponse;
import com.keystone.service.PartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    // =========================
    // CREATE PART
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartResponse createPart(
            @Valid @RequestBody PartRequest request) {

        return partService.createPart(request);
    }

    // =========================
    // GET ALL PARTS
    // =========================

    @GetMapping
    public List<PartResponse> getAllParts() {

        return partService.getAllParts();
    }

    // =========================
    // GET PART BY ID
    // =========================

    @GetMapping("/{id}")
    public PartResponse getPartById(
            @PathVariable Long id) {

        return partService.getPartById(id);
    }

    // =========================
    // UPDATE PART
    // =========================

    @PutMapping("/{id}")
    public PartResponse updatePart(
            @PathVariable Long id,
            @RequestBody PartRequest request) {

        return partService.updatePart(id, request);
    }

    // =========================
    // DELETE PART
    // =========================

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePart(
            @PathVariable Long id) {

        partService.deletePart(id);
    }
}