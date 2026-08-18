package com.keystone.service;

import com.keystone.dto.WorkOrderRequest;
import com.keystone.dto.WorkOrderResponse;
import com.keystone.entity.Customer;
import com.keystone.entity.Site;
import com.keystone.entity.Status;
import com.keystone.entity.User;
import com.keystone.entity.WorkOrder;
import com.keystone.repository.CustomerRepository;
import com.keystone.repository.SiteRepository;
import com.keystone.repository.UserRepository;
import com.keystone.repository.WorkOrderRepository;
import com.keystone.validation.WorkOrderStatusValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkOrderStatusHistoryService workOrderStatusHistoryService;


    // =========================================================
    // ASSIGN TECHNICIAN
    // =========================================================

    @Override
    public WorkOrderResponse assignTechnician(
            Long workOrderId,
            Long technicianId) {

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Work order not found with id: " + workOrderId
                ));

        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id: " + technicianId
                ));

        if (technician.getRole() != com.keystone.entity.Role.TECHNICIAN) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selected user is not a technician"
            );
        }

        Status oldStatus = workOrder.getStatus();

        workOrder.setTechnician(technician);

        if (oldStatus == Status.NEW) {

            Status newStatus = Status.ASSIGNED;

            WorkOrderStatusValidator.validateTransition(
                    oldStatus,
                    newStatus
            );

            workOrder.setStatus(newStatus);

            workOrderStatusHistoryService.createHistory(
                    workOrder.getId(),
                    oldStatus,
                    newStatus,
                    getCurrentUserId(),
                    "Technician assigned"
            );
        }

        WorkOrder savedWorkOrder =
                workOrderRepository.save(workOrder);

        return mapToResponse(savedWorkOrder);
    }


    // =========================================================
    // CREATE WORK ORDER
    // =========================================================

    @Override
    public WorkOrderResponse createWorkOrder(
            WorkOrderRequest request) {

        WorkOrder workOrder = new WorkOrder();

        workOrder.setTitle(request.getTitle());
        workOrder.setDescription(request.getDescription());
        workOrder.setPriority(request.getPriority());

        // Customer
        if (request.getCustomerId() != null) {

            Customer customer =
                    customerRepository.findById(
                            request.getCustomerId()
                    ).orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Customer not found with id: "
                                    + request.getCustomerId()
                    ));

            workOrder.setCustomer(customer);
        }

        // Site
        if (request.getSiteId() != null) {

            Site site =
                    siteRepository.findById(
                            request.getSiteId()
                    ).orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Site not found with id: "
                                    + request.getSiteId()
                    ));

            workOrder.setSite(site);
        }

        // Technician
        if (request.getTechnicianId() != null) {

            User technician =
                    userRepository.findById(
                            request.getTechnicianId()
                    ).orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Technician not found with id: "
                                    + request.getTechnicianId()
                    ));

            workOrder.setTechnician(technician);
        }

        WorkOrder savedWorkOrder =
                workOrderRepository.save(workOrder);

        return mapToResponse(savedWorkOrder);
    }


    // =========================================================
    // GET ALL WORK ORDERS
    // =========================================================

    @Override
    public List<WorkOrderResponse> getAllWorkOrders() {

        return workOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // GET WORK ORDER BY ID
    // =========================================================

    @Override
    public WorkOrderResponse getWorkOrderById(Long id) {

        WorkOrder workOrder =
                workOrderRepository.findById(id).orElse(null);

        if (workOrder == null) {
            return null;
        }

        return mapToResponse(workOrder);
    }


    // =========================================================
    // UPDATE WORK ORDER
    // =========================================================

    @Override
    public WorkOrderResponse updateWorkOrder(
            Long id,
            WorkOrderRequest request) {

        WorkOrder workOrder =
                workOrderRepository.findById(id).orElse(null);

        if (workOrder == null) {
            return null;
        }

        // Title
        if (request.getTitle() != null) {
            workOrder.setTitle(request.getTitle());
        }

        // Description
        if (request.getDescription() != null) {
            workOrder.setDescription(
                    request.getDescription()
            );
        }

        // Priority
        if (request.getPriority() != null) {
            workOrder.setPriority(
                    request.getPriority()
            );
        }

        // Customer
        if (request.getCustomerId() != null) {

            Customer customer =
                    customerRepository.findById(
                            request.getCustomerId()
                    ).orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Customer not found with id: "
                                    + request.getCustomerId()
                    ));

            workOrder.setCustomer(customer);
        }

        // Site
        if (request.getSiteId() != null) {

            Site site =
                    siteRepository.findById(
                            request.getSiteId()
                    ).orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Site not found with id: "
                                    + request.getSiteId()
                    ));

            workOrder.setSite(site);
        }

        // Technician
        if (request.getTechnicianId() != null) {

            User technician =
                    userRepository.findById(
                            request.getTechnicianId()
                    ).orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Technician not found with id: "
                                    + request.getTechnicianId()
                    ));

            workOrder.setTechnician(technician);
        }

        // Status
        if (request.getStatus() != null) {

            Status oldStatus =
                    workOrder.getStatus();

            Status newStatus =
                    request.getStatus();

            WorkOrderStatusValidator.validateTransition(
                    oldStatus,
                    newStatus
            );

            workOrder.setStatus(newStatus);

            workOrderStatusHistoryService.createHistory(
                    workOrder.getId(),
                    oldStatus,
                    newStatus,
                    getCurrentUserId(),
                    "Work order status changed"
            );
        }

        WorkOrder updatedWorkOrder =
                workOrderRepository.save(workOrder);

        return mapToResponse(updatedWorkOrder);
    }


    // =========================================================
    // DELETE WORK ORDER
    // =========================================================

    @Override
    @Transactional
    public void deleteWorkOrder(Long id) {

        System.out.println(
                "===== DELETE WORK ORDER ====="
        );

        System.out.println(
                "Deleting work order ID: " + id
        );

        // First check if work order exists
        if (!workOrderRepository.existsById(id)) {

            System.out.println(
                    "Work order not found: " + id
            );

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Work order not found with id: " + id
            );
        }

        // IMPORTANT:
        // Delete all status history records first.
        //
        // Otherwise PostgreSQL will reject deleting the
        // work order because work_order_status_history
        // contains a foreign key pointing to it.

        System.out.println(
                "Deleting status history for work order: " + id
        );

        workOrderStatusHistoryService
                .deleteHistoryByWorkOrderId(id);

        // Now delete the actual work order

        System.out.println(
                "Deleting work order: " + id
        );

        workOrderRepository.deleteById(id);

        System.out.println(
                "Work order deleted successfully: " + id
        );

        System.out.println(
                "=============================="
        );
    }


    // =========================================================
    // GET CURRENT USER ID
    // =========================================================

    private Long getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        System.out.println(
                "===== CURRENT USER DEBUG ====="
        );

        System.out.println(
                "Authentication: " + authentication
        );

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            System.out.println(
                    "Authentication missing or not authenticated"
            );

            return null;
        }

        System.out.println(
                "Username: " + authentication.getName()
        );

        System.out.println(
                "Principal: " + authentication.getPrincipal()
        );

        System.out.println(
                "Authorities: "
                        + authentication.getAuthorities()
        );

        String email =
                authentication.getName();

        User user =
                userRepository.findByEmail(email)
                        .orElse(null);

        System.out.println(
                "User found: " + user
        );

        if (user == null) {

            System.out.println(
                    "User NOT found for email: "
                            + email
            );

            return null;
        }

        System.out.println(
                "Current user ID: "
                        + user.getId()
        );

        System.out.println(
                "=============================="
        );

        return user.getId();
    }


    // =========================================================
    // MAP ENTITY TO RESPONSE
    // =========================================================

    private WorkOrderResponse mapToResponse(
            WorkOrder workOrder) {

        WorkOrderResponse response =
                new WorkOrderResponse();

        response.setId(
                workOrder.getId()
        );

        response.setTitle(
                workOrder.getTitle()
        );

        response.setDescription(
                workOrder.getDescription()
        );

        response.setPriority(
                workOrder.getPriority()
        );

        response.setStatus(
                workOrder.getStatus()
        );

        response.setCreatedDate(
                workOrder.getCreatedDate()
        );

        // Customer
        if (workOrder.getCustomer() != null) {

            response.setCustomerId(
                    workOrder.getCustomer().getId()
            );
        }

        // Site
        if (workOrder.getSite() != null) {

            response.setSiteId(
                    workOrder.getSite().getId()
            );
        }

        // Technician
        if (workOrder.getTechnician() != null) {

            response.setTechnicianId(
                    workOrder.getTechnician().getId()
            );

            String firstName =
                    workOrder.getTechnician()
                            .getFirstName();

            String lastName =
                    workOrder.getTechnician()
                            .getLastName();

            response.setTechnicianName(
                    firstName + " " + lastName
            );
        }

        return response;
    }
}