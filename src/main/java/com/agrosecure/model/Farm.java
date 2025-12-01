package com.agrosecure.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "farms")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String location;
    private UUID ownerId; // Linking to User ID logically or could be ManyToOne

    @CreatedDate
    private LocalDateTime createdAt;
}

