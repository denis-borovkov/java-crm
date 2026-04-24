package com.denisborovkov.javacrm.auth.token;

import com.denisborovkov.javacrm.auth.token.OTToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface OneTimeTokenMapper {
    OTToken toEntity(String tokenValue, String email, Instant issuedAt, Instant expiresAt);
}



