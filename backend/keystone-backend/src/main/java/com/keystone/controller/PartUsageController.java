package com.keystone.controller;

import com.keystone.dto.PartUsageRequest;
import com.keystone.dto.PartUsageResponse;
import com.keystone.service.PartUsageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/part-usage")
public class PartUsageController {

    private final PartUsageService partUsageService;

    public PartUsageController(
            PartUsageService partUsageService) {

        this.partUsageService = partUsageService;
    }

    // =========================
    // CREATE PART USAGE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartUsageResponse createPartUsage(
            @Valid @RequestBody PartUsageRequest request) {

        return partUsageService.createPartUsage(request);
    }

    // =========================
    // GET PART USAGE BY WORK ORDER
    // =========================

    @GetMapping("/workorder/{workOrderId}")
    public List<PartUsageResponse> getUsageByWorkOrder(
            @PathVariable Long workOrderId) {

        return partUsageService.getUsageByWorkOrder(
                workOrderId
        );
    }
}