package com.agrosecure.payload;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class LoginRequest {
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe contener @")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[a-zA-Z\\d@$!%*?&]{8,}$",
        message = "La contraseña debe tener mínimo 8 caracteres, letras, números y símbolos"
    )
    private String password;
}

