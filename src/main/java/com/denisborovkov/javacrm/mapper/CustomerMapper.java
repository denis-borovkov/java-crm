package com.denisborovkov.javacrm.mapper;

import com.denisborovkov.javacrm.dto.CustomerDTO;
import com.denisborovkov.javacrm.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toDTO(Customer customer);
}
