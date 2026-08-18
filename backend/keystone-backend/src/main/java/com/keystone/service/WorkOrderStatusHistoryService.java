package com.keystone.service;

import com.keystone.entity.Status;
import com.keystone.entity.WorkOrderStatusHistory;

import java.util.List;

public interface WorkOrderStatusHistoryService {

    WorkOrderStatusHistory createHistory(
            Long workOrderId,
            Status fromStatus,
            Status toStatus,
            Long changedBy,
            String note
    );

    List<WorkOrderStatusHistory> getHistoryByWorkOrderId(
            Long workOrderId
    );

    void deleteHistoryByWorkOrderId(
            Long workOrderId
    );
}