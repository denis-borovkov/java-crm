package com.denisborovkov.javacrm.mapper;

import com.denisborovkov.javacrm.entity.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    @Mapping(target = "token")
    @Mapping(target = "email")
    @Mapping(target = "revoked", constant = "false")
    @Mapping(target = "expiryDate")
    RefreshToken toEntity(String token, String email, Instant expiryDate);
}
