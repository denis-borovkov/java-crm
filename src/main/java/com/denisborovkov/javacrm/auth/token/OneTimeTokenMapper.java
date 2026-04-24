package com.denisborovkov.javacrm.auth.token;

import org.mapstruct.Mapper;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface OneTimeTokenMapper {
    OTToken toEntity(String tokenValue, String email, Instant issuedAt, Instant expiresAt);
}



