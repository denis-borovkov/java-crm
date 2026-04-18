package com.denisborovkov.javacrm.mapper;

import com.denisborovkov.javacrm.entity.OTToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface OneTimeTokenMapper {
    OTToken toEntity(String tokenValue, String email, Instant issuedAt, Instant expiresAt);
}
