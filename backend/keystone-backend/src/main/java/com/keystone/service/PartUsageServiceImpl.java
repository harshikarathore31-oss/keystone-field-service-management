package com.keystone.service;

import com.keystone.dto.PartUsageRequest;
import com.keystone.dto.PartUsageResponse;
import com.keystone.entity.Part;
import com.keystone.entity.PartUsage;
import com.keystone.entity.WorkOrder;
import com.keystone.repository.PartRepository;
import com.keystone.repository.PartUsageRepository;
import com.keystone.repository.WorkOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartUsageServiceImpl implements PartUsageService {

    private final PartUsageRepository partUsageRepository;
    private final PartRepository partRepository;
    private final WorkOrderRepository workOrderRepository;

    public PartUsageServiceImpl(
            PartUsageRepository partUsageRepository,
            PartRepository partRepository,
            WorkOrderRepository workOrderRepository) {

        this.partUsageRepository = partUsageRepository;
        this.partRepository = partRepository;
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional
    public PartUsageResponse createPartUsage(PartUsageRequest request) {

        // =========================
        // FIND WORK ORDER
        // =========================

        WorkOrder workOrder = workOrderRepository
                .findById(request.getWorkOrderId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Work order not found with id: "
                                + request.getWorkOrderId()
                ));

        // =========================
        // FIND PART
        // =========================

        Part part = partRepository
                .findById(request.getPartId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Part not found with id: "
                                + request.getPartId()
                ));

        // =========================
        // CHECK STOCK
        // =========================

        if (part.getStockQuantity() < request.getQuantity()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Insufficient stock. Available: "
                            + part.getStockQuantity()
                            + ", requested: "
                            + request.getQuantity()
            );
        }

        // =========================
        // REDUCE STOCK
        // =========================

        part.setStockQuantity(
                part.getStockQuantity() - request.getQuantity()
        );

        partRepository.save(part);

        // =========================
        // CREATE PART USAGE
        // =========================

        PartUsage partUsage = new PartUsage();

        partUsage.setWorkOrder(workOrder);
        partUsage.setPart(part);
        partUsage.setQuantity(request.getQuantity());

        PartUsage savedUsage =
                partUsageRepository.save(partUsage);

        return mapToResponse(savedUsage);
    }

    // =========================
    // GET USAGE FOR WORK ORDER
    // =========================

    @Override
    public List<PartUsageResponse> getUsageByWorkOrder(
            Long workOrderId) {

        // First make sure work order exists

        if (!workOrderRepository.existsById(workOrderId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Work order not found with id: "
                            + workOrderId
            );
        }

        return partUsageRepository
                .findByWorkOrderId(workOrderId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // MAP ENTITY TO RESPONSE
    // =========================

    private PartUsageResponse mapToResponse(
            PartUsage partUsage) {

        PartUsageResponse response =
                new PartUsageResponse();

        response.setId(partUsage.getId());

        if (partUsage.getWorkOrder() != null) {

            response.setWorkOrderId(
                    partUsage.getWorkOrder().getId()
            );
        }

        if (partUsage.getPart() != null) {

            response.setPartId(
                    partUsage.getPart().getId()
            );

            response.setPartName(
                    partUsage.getPart().getName()
            );
        }

        response.setQuantity(
                partUsage.getQuantity()
        );

        response.setUsedAt(
                partUsage.getUsedAt()
        );

        return response;
    }
}