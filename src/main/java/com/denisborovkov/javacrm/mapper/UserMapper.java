package com.denisborovkov.javacrm.mapper;

import com.denisborovkov.javacrm.dto.auth.CreateAdminRequest;
import com.denisborovkov.javacrm.dto.auth.SignupRequest;
import com.denisborovkov.javacrm.dto.entity.UserDTO;
import com.denisborovkov.javacrm.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(SignupRequest request);
    UserEntity toEntity(CreateAdminRequest request);
    UserDTO toDTO(UserEntity userEntity);
}