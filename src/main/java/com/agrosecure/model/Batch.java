package com.agrosecure.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "batches")
@Data
@NoArgsConstructor
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String batchCode;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id")
    private Crop crop;
}

