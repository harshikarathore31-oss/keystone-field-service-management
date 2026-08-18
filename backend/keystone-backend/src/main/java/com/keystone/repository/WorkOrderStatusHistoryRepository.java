package com.keystone.repository;

import com.keystone.entity.WorkOrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderStatusHistoryRepository
        extends JpaRepository<WorkOrderStatusHistory, Long> {

    List<WorkOrderStatusHistory> findByWorkOrderIdOrderByChangedAtAsc(
            Long workOrderId
    );

    @Modifying
    @Query("DELETE FROM WorkOrderStatusHistory h WHERE h.workOrder.id = :workOrderId")
    void deleteByWorkOrderId(
            @Param("workOrderId") Long workOrderId
    );
}