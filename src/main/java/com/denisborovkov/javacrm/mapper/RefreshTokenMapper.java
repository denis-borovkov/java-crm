package com.denisborovkov.javacrm.mapper;

import com.denisborovkov.javacrm.entity.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    @Mapping(target = "revoked", constant = "false")
    RefreshToken toEntity(String token, String email, Instant expiryDate);
}
