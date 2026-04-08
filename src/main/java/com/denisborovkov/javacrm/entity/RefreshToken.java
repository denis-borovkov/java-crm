package com.denisborovkov.javacrm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {

    @Id
    private String token;
    private String email;
    private boolean revoked;
    private Instant expiryDate;
}
