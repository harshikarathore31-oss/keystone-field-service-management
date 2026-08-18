package com.keystone.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TimeLogRequest {

    @NotNull(message = "Work order ID is required")
    private Long workOrderId;

    @NotNull(message = "Technician ID is required")
    private Long technicianId;

    @NotNull(message = "Minutes are required")
    @Positive(message = "Minutes must be greater than 0")
    private Integer minutes;

    private String note;

    // =========================
    // WORK ORDER ID
    // =========================

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    // =========================
    // TECHNICIAN ID
    // =========================

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    // =========================
    // MINUTES
    // =========================

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    // =========================
    // NOTE
    // =========================

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}