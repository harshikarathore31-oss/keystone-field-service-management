package com.keystone.dto;

import java.time.LocalDateTime;

public class TimeLogResponse {

    private Long id;

    private Long workOrderId;

    private Long technicianId;

    private String technicianName;

    private Integer minutes;

    private String note;

    private LocalDateTime loggedAt;

    // =========================
    // ID
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
    // TECHNICIAN NAME
    // =========================

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
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

    // =========================
    // LOGGED AT
    // =========================

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }
}