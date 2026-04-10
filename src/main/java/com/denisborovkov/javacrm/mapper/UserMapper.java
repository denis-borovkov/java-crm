package com.denisborovkov.javacrm.mapper;

import com.denisborovkov.javacrm.dto.CreateAdminRequest;
import com.denisborovkov.javacrm.dto.SignupRequest;
import com.denisborovkov.javacrm.dto.UserDTO;
import com.denisborovkov.javacrm.entity.UserEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email")
    @Mapping(target = "password")
    UserEntity toEntity(SignupRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email")
    @Mapping(target = "password")
    UserEntity toEntity(CreateAdminRequest request);

    UserDTO toDTO(UserDetails userEntity);
}