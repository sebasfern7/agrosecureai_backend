package com.agrosecure.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UpdateFarmInput {
    private UUID id;
    private String name;
    private String location;
}







