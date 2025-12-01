package com.agrosecure.repository;

import com.agrosecure.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BatchRepository extends JpaRepository<Batch, UUID> {
    List<Batch> findByCropId(UUID cropId);
}
