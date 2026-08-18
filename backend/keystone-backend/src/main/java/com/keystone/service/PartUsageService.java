package com.keystone.service;

import com.keystone.dto.PartUsageRequest;
import com.keystone.dto.PartUsageResponse;

import java.util.List;

public interface PartUsageService {

    PartUsageResponse createPartUsage(PartUsageRequest request);

    List<PartUsageResponse> getUsageByWorkOrder(Long workOrderId);
}