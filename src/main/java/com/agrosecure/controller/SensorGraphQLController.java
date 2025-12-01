package com.agrosecure.controller;

import com.agrosecure.dto.SensorDataInput;
import com.agrosecure.model.Batch;
import com.agrosecure.model.Crop;
import com.agrosecure.model.Farm;
import com.agrosecure.model.SensorData;
import com.agrosecure.repository.BatchRepository;
import com.agrosecure.repository.CropRepository;
import com.agrosecure.repository.FarmRepository;
import com.agrosecure.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class SensorGraphQLController {
    @Autowired
    SensorDataRepository sensorDataRepository;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    CropRepository cropRepository;

    @Autowired
    FarmRepository farmRepository;

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public SensorData addSensorData(@Argument SensorDataInput input) {
        Batch batch = batchRepository.findById(input.getBatchId())
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        SensorData data = new SensorData();
        data.setMetric(input.getMetric());
        data.setValue(input.getValue());
        data.setBatch(batch);
        data.setTimestamp(LocalDateTime.now());

        return sensorDataRepository.save(data);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public List<SensorData> farmMetrics(@Argument UUID farmId) {
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() -> new RuntimeException("Farm not found"));

        List<Crop> crops = cropRepository.findByFarmId(farm.getId());
        List<SensorData> allData = new ArrayList<>();

        for (Crop crop : crops) {
            List<Batch> batches = batchRepository.findByCropId(crop.getId());
            for (Batch batch : batches) {
                allData.addAll(sensorDataRepository.findByBatchId(batch.getId()));
            }
        }
        
        return allData;
    }
}
