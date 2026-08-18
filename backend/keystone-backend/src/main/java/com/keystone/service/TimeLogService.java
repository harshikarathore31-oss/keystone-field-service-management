package com.keystone.service;

import com.keystone.dto.TimeLogRequest;
import com.keystone.dto.TimeLogResponse;

import java.util.List;

public interface TimeLogService {

    TimeLogResponse createTimeLog(TimeLogRequest request);

    List<TimeLogResponse> getAllTimeLogs();

    TimeLogResponse getTimeLogById(Long id);

    List<TimeLogResponse> getTimeLogsByWorkOrder(Long workOrderId);

    List<TimeLogResponse> getTimeLogsByTechnician(Long technicianId);

    void deleteTimeLog(Long id);
}