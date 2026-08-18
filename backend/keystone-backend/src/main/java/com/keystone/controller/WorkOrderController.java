package com.keystone.controller;

import com.keystone.dto.WorkOrderRequest;
import com.keystone.dto.WorkOrderResponse;
import com.keystone.service.WorkOrderService;

import jakarta.validation.Valid;
import com.keystone.dto.AssignTechnicianRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workorders")
public class WorkOrderController {

    @PatchMapping("/{id}/assign")
    public WorkOrderResponse assignTechnician(
            @PathVariable Long id,
            @Valid @RequestBody AssignTechnicianRequest request) {

        return workOrderService.assignTechnician(
                id,
                request.getTechnicianId()
        );
    }
    @Autowired
    private WorkOrderService workOrderService;


    // =========================
    // CREATE WORK ORDER
    // =========================

    @PostMapping
    public WorkOrderResponse createWorkOrder(
            @Valid @RequestBody WorkOrderRequest request) {

        return workOrderService.createWorkOrder(request);
    }


    // =========================
    // GET ALL WORK ORDERS
    // =========================

    @GetMapping
    public List<WorkOrderResponse> getAllWorkOrders() {

        return workOrderService.getAllWorkOrders();
    }


    // =========================
    // GET WORK ORDER BY ID
    // =========================

    @GetMapping("/{id}")
    public WorkOrderResponse getWorkOrderById(
            @PathVariable Long id) {

        return workOrderService.getWorkOrderById(id);
    }


    // =========================
    // UPDATE WORK ORDER
    // =========================

    @PutMapping("/{id}")
    public WorkOrderResponse updateWorkOrder(
            @PathVariable Long id,
            @Valid @RequestBody WorkOrderRequest request) {

        return workOrderService.updateWorkOrder(id, request);
    }


    // =========================
    // DELETE WORK ORDER
    // =========================

    @DeleteMapping("/{id}")
    public void deleteWorkOrder(
            @PathVariable Long id) {

        workOrderService.deleteWorkOrder(id);
    }
}