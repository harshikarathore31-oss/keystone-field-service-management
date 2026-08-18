package com.keystone.service;

import com.keystone.dto.SLARequest;
import com.keystone.dto.SLAResponse;

import java.util.List;

public interface SLAService {

    SLAResponse createSLA(SLARequest request);

    List<SLAResponse> getAllSLAs();

    List<SLAResponse> getActiveSLAs();

    SLAResponse getSLAById(Long id);

    SLAResponse updateSLA(Long id, SLARequest request);

    void deleteSLA(Long id);
}