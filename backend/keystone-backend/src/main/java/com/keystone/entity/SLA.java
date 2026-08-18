package com.keystone.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "slas")
public class SLA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // SLA name
    @Column(nullable = false)
    private String name;

    // Priority this SLA applies to
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    // Maximum time allowed to respond, in minutes
    @Column(nullable = false)
    private Integer responseTimeMinutes;

    // Maximum time allowed to resolve, in minutes
    @Column(nullable = false)
    private Integer resolutionTimeMinutes;

    // Whether this SLA is currently active
    @Column(nullable = false)
    private Boolean active;

    private LocalDateTime createdAt;

    public SLA() {
    }

    // =========================
    // PRE PERSIST
    // =========================

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();

        if (active == null) {
            active = true;
        }
    }

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

    // =========================
    // CREATED AT
    // =========================

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}