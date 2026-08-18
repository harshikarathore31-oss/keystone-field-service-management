package com.keystone.controller;

import com.keystone.dto.WorkOrderStatusHistoryResponse;
import com.keystone.entity.WorkOrderStatusHistory;
import com.keystone.service.WorkOrderStatusHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workorders")
public class WorkOrderStatusHistoryController {

    @Autowired
    private WorkOrderStatusHistoryService historyService;

    @GetMapping("/{workOrderId}/history")
    public List<WorkOrderStatusHistoryResponse> getWorkOrderHistory(
            @PathVariable Long workOrderId) {

        return historyService
                .getHistoryByWorkOrderId(workOrderId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private WorkOrderStatusHistoryResponse mapToResponse(
            WorkOrderStatusHistory history) {

        WorkOrderStatusHistoryResponse response =
                new WorkOrderStatusHistoryResponse();

        response.setId(history.getId());

        if (history.getWorkOrder() != null) {
            response.setWorkOrderId(
                    history.getWorkOrder().getId()
            );
        }

        response.setFromStatus(history.getFromStatus());
        response.setToStatus(history.getToStatus());

        if (history.getChangedBy() != null) {

            response.setChangedById(
                    history.getChangedBy().getId()
            );

            String firstName =
                    history.getChangedBy().getFirstName();

            String lastName =
                    history.getChangedBy().getLastName();

            response.setChangedByName(
                    firstName + " " + lastName
            );
        }

        response.setChangedAt(history.getChangedAt());
        response.setNote(history.getNote());

        return response;
    }
}