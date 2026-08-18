package com.keystone.service;

import com.keystone.dto.PartRequest;
import com.keystone.dto.PartResponse;

import java.util.List;

public interface PartService {

    PartResponse createPart(PartRequest request);

    List<PartResponse> getAllParts();

    PartResponse getPartById(Long id);

    PartResponse updatePart(Long id, PartRequest request);

    void deletePart(Long id);
}