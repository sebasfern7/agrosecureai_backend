package com.agrosecure.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SensorDataInput {
    private UUID batchId;
    private String metric;
    private Double value;
}

