package com.agrosecure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class CreateCropDTO {

    @NotBlank(message = "Nombre del cultivo es requerido")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Nombre del cultivo solo debe contener letras")
    private String type;

    @NotBlank(message = "Variedad es requerida")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Variedad solo debe contener letras")
    private String variety;

    @NotBlank(message = "Área es requerida")
    // Si el área puede ser numérica, mejor usar otra validación:
    @Pattern(regexp = "^[0-9]+$", message = "Área solo debe contener números")
    private String area;

    @NotBlank(message = "Fecha de siembra es requerida")
    private String plantedDate; // Recomendable usar LocalDate para fechas

    @NotBlank(message = "Fecha de cosecha es requerida")
    private String harvestDate; // Recomendable usar LocalDate para fechas

    // Getters y setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPlantedDate() {
        return plantedDate;
    }

    public void setPlantedDate(String plantedDate) {
        this.plantedDate = plantedDate;
    }

    public String getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(String harvestDate) {
        this.harvestDate = harvestDate;
    }
}
