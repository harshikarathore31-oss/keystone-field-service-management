package com.keystone.service;

import com.keystone.dto.TimeLogRequest;
import com.keystone.dto.TimeLogResponse;
import com.keystone.entity.TimeLog;
import com.keystone.entity.User;
import com.keystone.entity.WorkOrder;
import com.keystone.repository.TimeLogRepository;
import com.keystone.repository.UserRepository;
import com.keystone.repository.WorkOrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeLogServiceImpl implements TimeLogService {

    @Autowired
    private TimeLogRepository timeLogRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private UserRepository userRepository;

    // =========================
    // CREATE TIME LOG
    // =========================

    @Override
    public TimeLogResponse createTimeLog(TimeLogRequest request) {

        WorkOrder workOrder = workOrderRepository
                .findById(request.getWorkOrderId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Work order not found with id: "
                                + request.getWorkOrderId()
                ));

        User technician = userRepository
                .findById(request.getTechnicianId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Technician not found with id: "
                                + request.getTechnicianId()
                ));

        TimeLog timeLog = new TimeLog();

        timeLog.setWorkOrder(workOrder);
        timeLog.setTechnician(technician);
        timeLog.setMinutes(request.getMinutes());
        timeLog.setNote(request.getNote());

        TimeLog savedTimeLog = timeLogRepository.save(timeLog);

        return mapToResponse(savedTimeLog);
    }

    // =========================
    // GET ALL TIME LOGS
    // =========================

    @Override
    public List<TimeLogResponse> getAllTimeLogs() {

        return timeLogRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // GET TIME LOG BY ID
    // =========================

    @Override
    public TimeLogResponse getTimeLogById(Long id) {

        TimeLog timeLog = timeLogRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Time log not found with id: " + id
                ));

        return mapToResponse(timeLog);
    }

    // =========================
    // GET BY WORK ORDER
    // =========================

    @Override
    public List<TimeLogResponse> getTimeLogsByWorkOrder(Long workOrderId) {

        // First verify that work order exists
        if (!workOrderRepository.existsById(workOrderId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Work order not found with id: " + workOrderId
            );
        }

        return timeLogRepository
                .findByWorkOrderId(workOrderId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // GET BY TECHNICIAN
    // =========================

    @Override
    public List<TimeLogResponse> getTimeLogsByTechnician(Long technicianId) {

        // First verify that technician exists
        if (!userRepository.existsById(technicianId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Technician not found with id: " + technicianId
            );
        }

        return timeLogRepository
                .findByTechnicianId(technicianId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // DELETE TIME LOG
    // =========================

    @Override
    public void deleteTimeLog(Long id) {

        if (!timeLogRepository.existsById(id)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Time log not found with id: " + id
            );
        }

        timeLogRepository.deleteById(id);
    }

    // =========================
    // MAP ENTITY TO RESPONSE
    // =========================

    private TimeLogResponse mapToResponse(TimeLog timeLog) {

        TimeLogResponse response = new TimeLogResponse();

        response.setId(timeLog.getId());

        if (timeLog.getWorkOrder() != null) {
            response.setWorkOrderId(
                    timeLog.getWorkOrder().getId()
            );
        }

        if (timeLog.getTechnician() != null) {

            response.setTechnicianId(
                    timeLog.getTechnician().getId()
            );

            String firstName = timeLog.getTechnician().getFirstName();
            String lastName = timeLog.getTechnician().getLastName();

            String technicianName =
                    ((firstName != null ? firstName : "") + " "
                            + (lastName != null ? lastName : "")).trim();

            response.setTechnicianName(technicianName);
        }

        response.setMinutes(timeLog.getMinutes());
        response.setNote(timeLog.getNote());
        response.setLoggedAt(timeLog.getLoggedAt());

        return response;
    }
}