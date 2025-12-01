package com.agrosecure.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "crops")
@Data
@NoArgsConstructor
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String type; // Trigo, Maíz, etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id")
    private Farm farm;
}

