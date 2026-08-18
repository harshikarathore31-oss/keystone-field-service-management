package com.keystone.service;

import com.keystone.dto.WorkOrderRequest;
import com.keystone.dto.WorkOrderResponse;

import java.util.List;

public interface WorkOrderService {

    WorkOrderResponse createWorkOrder(WorkOrderRequest request);

    List<WorkOrderResponse> getAllWorkOrders();

    WorkOrderResponse getWorkOrderById(Long id);

    WorkOrderResponse updateWorkOrder(Long id, WorkOrderRequest request);

    WorkOrderResponse assignTechnician(Long workOrderId, Long technicianId);

    void deleteWorkOrder(Long id);
}