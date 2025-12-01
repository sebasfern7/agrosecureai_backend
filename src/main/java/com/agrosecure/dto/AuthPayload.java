package com.agrosecure.dto;

import com.agrosecure.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthPayload {
    private String accessToken;
    private User user;
}

