package com.denisborovkov.javacrm.mapper;

import com.denisborovkov.javacrm.dto.CreateAdminRequest;
import com.denisborovkov.javacrm.dto.SignupRequest;
import com.denisborovkov.javacrm.dto.UserDTO;
import com.denisborovkov.javacrm.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.password()))")
    @Mapping(target = "role", constant = "USER")
    User toUserEntity(SignupRequest request, PasswordEncoder passwordEncoder);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.password()))")
    @Mapping(target = "role", constant = "ADMIN")
    User toAdminEntity(CreateAdminRequest request, PasswordEncoder passwordEncoder);

    UserDTO toDTO(User user);
}
