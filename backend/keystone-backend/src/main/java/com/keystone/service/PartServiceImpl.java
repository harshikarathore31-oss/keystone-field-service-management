package com.keystone.service;

import com.keystone.dto.PartRequest;
import com.keystone.dto.PartResponse;
import com.keystone.entity.Part;
import com.keystone.repository.PartRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;

    public PartServiceImpl(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Override
    public PartResponse createPart(PartRequest request) {

        Part part = new Part();

        part.setName(request.getName());
        part.setStockQuantity(request.getStockQuantity());
        part.setUnitPrice(request.getUnitPrice());

        Part savedPart = partRepository.save(part);

        return mapToResponse(savedPart);
    }

    @Override
    public List<PartResponse> getAllParts() {

        return partRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PartResponse getPartById(Long id) {

        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Part not found with id: " + id
                ));

        return mapToResponse(part);
    }

    @Override
    public PartResponse updatePart(Long id, PartRequest request) {

        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Part not found with id: " + id
                ));

        if (request.getName() != null) {
            part.setName(request.getName());
        }

        if (request.getStockQuantity() != null) {
            if (request.getStockQuantity() < 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Stock quantity cannot be negative"
                );
            }

            part.setStockQuantity(request.getStockQuantity());
        }

        if (request.getUnitPrice() != null) {
            if (request.getUnitPrice().signum() < 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Unit price cannot be negative"
                );
            }

            part.setUnitPrice(request.getUnitPrice());
        }

        Part updatedPart = partRepository.save(part);

        return mapToResponse(updatedPart);
    }

    @Override
    public void deletePart(Long id) {

        if (!partRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Part not found with id: " + id
            );
        }

        partRepository.deleteById(id);
    }

    private PartResponse mapToResponse(Part part) {

        PartResponse response = new PartResponse();

        response.setId(part.getId());
        response.setName(part.getName());
        response.setStockQuantity(part.getStockQuantity());
        response.setUnitPrice(part.getUnitPrice());

        return response;
    }
}