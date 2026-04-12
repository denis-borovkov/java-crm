package com.denisborovkov.javacrm.mapper;

import com.denisborovkov.javacrm.dto.entity.CreateCustomerRequest;
import com.denisborovkov.javacrm.dto.entity.CustomerDTO;
import com.denisborovkov.javacrm.dto.entity.UpdateCustomerRequest;
import com.denisborovkov.javacrm.dto.entity.UpdateCustomerResponse;
import com.denisborovkov.javacrm.entity.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toDTO(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "status", constant = "ACTIVE")
    Customer toEntity(CreateCustomerRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomer(UpdateCustomerRequest updateCustomerRequest,
                        @MappingTarget Customer entity);

    @Mapping(target = "customerId")
    UpdateCustomerResponse toUpdateResponse(Customer customer);
}
