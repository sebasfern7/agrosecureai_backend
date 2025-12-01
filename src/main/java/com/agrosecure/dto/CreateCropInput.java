package com.agrosecure.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CreateCropInput {
    private UUID farmId;
    private String type;
}


