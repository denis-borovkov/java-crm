package com.denisborovkov.javacrm.mapper;

import com.denisborovkov.javacrm.dto.UserDTO;
import com.denisborovkov.javacrm.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
}
