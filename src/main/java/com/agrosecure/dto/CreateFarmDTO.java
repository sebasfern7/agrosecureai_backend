package com.agrosecure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateFarmDTO {

    @NotBlank(message = "Nombre de la granja es requerido")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Nombre de la granja solo debe contener letras")
    private String name;

    @NotBlank(message = "Ubicación es requerida")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Ubicación solo debe contener letras")
    private String location;

    // Getters and Setters
}