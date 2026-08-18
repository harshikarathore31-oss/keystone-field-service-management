package com.keystone.controller;

import com.keystone.dto.TimeLogRequest;
import com.keystone.dto.TimeLogResponse;
import com.keystone.service.TimeLogService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/time-logs")
public class TimeLogController {

    @Autowired
    private TimeLogService timeLogService;

    // =========================
    // CREATE TIME LOG
    // =========================

    @PostMapping
    public TimeLogResponse createTimeLog(
            @Valid @RequestBody TimeLogRequest request) {

        return timeLogService.createTimeLog(request);
    }

    // =========================
    // GET ALL TIME LOGS
    // =========================

    @GetMapping
    public List<TimeLogResponse> getAllTimeLogs() {

        return timeLogService.getAllTimeLogs();
    }

    // =========================
    // GET TIME LOG BY ID
    // =========================

    @GetMapping("/{id}")
    public TimeLogResponse getTimeLogById(
            @PathVariable Long id) {

        return timeLogService.getTimeLogById(id);
    }

    // =========================
    // GET TIME LOGS BY WORK ORDER
    // =========================

    @GetMapping("/workorder/{workOrderId}")
    public List<TimeLogResponse> getTimeLogsByWorkOrder(
            @PathVariable Long workOrderId) {

        return timeLogService.getTimeLogsByWorkOrder(workOrderId);
    }

    // =========================
    // GET TIME LOGS BY TECHNICIAN
    // =========================

    @GetMapping("/technician/{technicianId}")
    public List<TimeLogResponse> getTimeLogsByTechnician(
            @PathVariable Long technicianId) {

        return timeLogService.getTimeLogsByTechnician(technicianId);
    }

    // =========================
    // DELETE TIME LOG
    // =========================

    @DeleteMapping("/{id}")
    public void deleteTimeLog(
            @PathVariable Long id) {

        timeLogService.deleteTimeLog(id);
    }
}