package com.denisborovkov.javacrm.mapper;

import com.denisborovkov.javacrm.entity.OTToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface OneTimeTokenMapper {

    @Mapping(target = "tokenValue")
    @Mapping(target = "email")
    @Mapping(target = "issuedAt")
    @Mapping(target = "expiresAt")
    OTToken toEntity(String tokenValue, String email, Instant issuedAt, Instant expiresAt);
}
