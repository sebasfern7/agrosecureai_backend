package com.agrosecure.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private UUID id;
    private String email;
    private String tenantId;
    private List<String> roles;
}

