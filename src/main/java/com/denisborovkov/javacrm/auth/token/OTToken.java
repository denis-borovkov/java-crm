package com.denisborovkov.javacrm.auth.token;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OTToken {

    @Id
    private String tokenValue;
    private String email;
    private Instant issuedAt;
    private Instant expiresAt;
}



