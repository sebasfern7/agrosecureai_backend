package com.agrosecure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    @NotBlank(message = "Nombre es requerido")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Nombre solo debe contener letras")
    private String firstName;

    @NotBlank(message = "Apellido es requerido")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Apellido solo debe contener letras")
    private String lastName;

    @NotBlank(message = "Nombre de la granja es requerido")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Nombre de la granja solo debe contener letras")
    private String farmName;

    @NotBlank(message = "Email es requerido")
    @Email(message = "Correo electrónico inválido")
    private String email;

    @NotBlank(message = "Contraseña es requerida")
    @Size(min = 8, message = "Contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Contraseña debe tener al menos 8 caracteres, una letra, un número y un símbolo")
    private String password;

    // Getters and Setters
}