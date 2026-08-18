package com.keystone.service;

import com.keystone.dto.SLARequest;
import com.keystone.dto.SLAResponse;
import com.keystone.entity.SLA;
import com.keystone.repository.SLARepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SLAServiceImpl implements SLAService {

    private final SLARepository slaRepository;

    public SLAServiceImpl(SLARepository slaRepository) {
        this.slaRepository = slaRepository;
    }

    @Override
    public SLAResponse createSLA(SLARequest request) {

        // Check whether an active SLA already exists
        // for the same priority.
        if (request.getActive() == null || request.getActive()) {

            slaRepository.findByPriorityAndActiveTrue(request.getPriority())
                    .ifPresent(existingSLA -> {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Active SLA already exists for priority: "
                                        + request.getPriority()
                        );
                    });
        }

        SLA sla = new SLA();

        sla.setName(request.getName());
        sla.setPriority(request.getPriority());
        sla.setResponseTimeMinutes(
                request.getResponseTimeMinutes()
        );
        sla.setResolutionTimeMinutes(
                request.getResolutionTimeMinutes()
        );

        if (request.getActive() == null) {
            sla.setActive(true);
        } else {
            sla.setActive(request.getActive());
        }

        SLA savedSLA = slaRepository.save(sla);

        return mapToResponse(savedSLA);
    }

    @Override
    public List<SLAResponse> getAllSLAs() {

        return slaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SLAResponse> getActiveSLAs() {

        return slaRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SLAResponse getSLAById(Long id) {

        SLA sla = slaRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "SLA not found with id: " + id
                        )
                );

        return mapToResponse(sla);
    }

    @Override
    public SLAResponse updateSLA(
            Long id,
            SLARequest request) {

        SLA sla = slaRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "SLA not found with id: " + id
                        )
                );

        if (request.getName() != null) {
            sla.setName(request.getName());
        }

        if (request.getPriority() != null) {
            sla.setPriority(request.getPriority());
        }

        if (request.getResponseTimeMinutes() != null) {
            sla.setResponseTimeMinutes(
                    request.getResponseTimeMinutes()
            );
        }

        if (request.getResolutionTimeMinutes() != null) {
            sla.setResolutionTimeMinutes(
                    request.getResolutionTimeMinutes()
            );
        }

        if (request.getActive() != null) {
            sla.setActive(request.getActive());
        }

        SLA updatedSLA = slaRepository.save(sla);

        return mapToResponse(updatedSLA);
    }

    @Override
    public void deleteSLA(Long id) {

        if (!slaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "SLA not found with id: " + id
            );
        }

        slaRepository.deleteById(id);
    }

    // =========================
    // ENTITY → RESPONSE
    // =========================

    private SLAResponse mapToResponse(SLA sla) {

        SLAResponse response = new SLAResponse();

        response.setId(sla.getId());
        response.setName(sla.getName());
        response.setPriority(sla.getPriority());
        response.setResponseTimeMinutes(
                sla.getResponseTimeMinutes()
        );
        response.setResolutionTimeMinutes(
                sla.getResolutionTimeMinutes()
        );
        response.setActive(sla.getActive());
        response.setCreatedAt(sla.getCreatedAt());

        return response;
    }
}