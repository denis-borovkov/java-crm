package com.denisborovkov.javacrm.mapper;

import com.denisborovkov.javacrm.dto.auth.CreateAdminRequest;
import com.denisborovkov.javacrm.dto.auth.SignupRequest;
import com.denisborovkov.javacrm.dto.entity.UserDTO;
import com.denisborovkov.javacrm.entity.UserEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

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

    UserDTO toDTO(UserEntity userEntity);
}