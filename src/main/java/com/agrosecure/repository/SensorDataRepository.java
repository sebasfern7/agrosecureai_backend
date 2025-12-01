package com.agrosecure.repository;

import com.agrosecure.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, UUID> {
    List<SensorData> findByBatchId(UUID batchId);
    // Potentially useful for aggregating data for a farm?
    // Would need a custom query or findByBatch_Crop_Farm_Id
}

