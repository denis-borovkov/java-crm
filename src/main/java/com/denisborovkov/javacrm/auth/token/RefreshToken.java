package com.denisborovkov.javacrm.auth.token;

import jakarta.persistence.Column;
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
    @Column(length = 512)
    private String token;
    private String email;
    private boolean revoked;
    private Instant expiryDate;
}



