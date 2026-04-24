package com.denisborovkov.javacrm.user.mapper;

import com.denisborovkov.javacrm.auth.dto.CreateAdminRequest;
import com.denisborovkov.javacrm.auth.dto.SignupRequest;
import com.denisborovkov.javacrm.user.dto.UserDTO;
import com.denisborovkov.javacrm.user.domain.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(SignupRequest request);
    UserEntity toEntity(CreateAdminRequest request);
    UserDTO toDTO(UserEntity userEntity);
}


