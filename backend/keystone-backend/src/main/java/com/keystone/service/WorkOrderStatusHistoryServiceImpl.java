package com.keystone.service;

import com.keystone.entity.Status;
import com.keystone.entity.User;
import com.keystone.entity.WorkOrder;
import com.keystone.entity.WorkOrderStatusHistory;
import com.keystone.repository.UserRepository;
import com.keystone.repository.WorkOrderRepository;
import com.keystone.repository.WorkOrderStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkOrderStatusHistoryServiceImpl
        implements WorkOrderStatusHistoryService {

    @Autowired
    private WorkOrderStatusHistoryRepository historyRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public WorkOrderStatusHistory createHistory(
            Long workOrderId,
            Status fromStatus,
            Status toStatus,
            Long changedBy,
            String note) {

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "WorkOrder not found with id: " + workOrderId
                        )
                );

        User user = null;

        if (changedBy != null) {
            user = userRepository.findById(changedBy)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User not found with id: " + changedBy
                            )
                    );
        }

        WorkOrderStatusHistory history =
                new WorkOrderStatusHistory();

        history.setWorkOrder(workOrder);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setChangedBy(user);
        history.setChangedAt(LocalDateTime.now());
        history.setNote(note);

        return historyRepository.save(history);
    }

    @Override
    public List<WorkOrderStatusHistory> getHistoryByWorkOrderId(
            Long workOrderId) {

        return historyRepository
                .findByWorkOrderIdOrderByChangedAtAsc(workOrderId);
    }

    @Override
    public void deleteHistoryByWorkOrderId(
            Long workOrderId) {

        historyRepository.deleteByWorkOrderId(workOrderId);
    }
}