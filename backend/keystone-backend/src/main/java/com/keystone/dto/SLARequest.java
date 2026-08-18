package com.keystone.dto;

import com.keystone.entity.Priority;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SLARequest {

    @NotBlank(message = "SLA name is required")
    private String name;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Response time is required")
    @Min(value = 1, message = "Response time must be greater than 0")
    private Integer responseTimeMinutes;

    @NotNull(message = "Resolution time is required")
    @Min(value = 1, message = "Resolution time must be greater than 0")
    private Integer resolutionTimeMinutes;

    private Boolean active;

    // =========================
    // NAME
    // =========================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // =========================
    // PRIORITY
    // =========================

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    // =========================
    // RESPONSE TIME
    // =========================

    public Integer getResponseTimeMinutes() {
        return responseTimeMinutes;
    }

    public void setResponseTimeMinutes(Integer responseTimeMinutes) {
        this.responseTimeMinutes = responseTimeMinutes;
    }

    // =========================
    // RESOLUTION TIME
    // =========================

    public Integer getResolutionTimeMinutes() {
        return resolutionTimeMinutes;
    }

    public void setResolutionTimeMinutes(Integer resolutionTimeMinutes) {
        this.resolutionTimeMinutes = resolutionTimeMinutes;
    }

    // =========================
    // ACTIVE
    // =========================

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}