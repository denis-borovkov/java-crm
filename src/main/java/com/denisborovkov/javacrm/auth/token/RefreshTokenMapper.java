package com.denisborovkov.javacrm.auth.token;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    @Mapping(target = "revoked", constant = "false")
    RefreshToken toEntity(String token, String email, Instant expiryDate);
}



