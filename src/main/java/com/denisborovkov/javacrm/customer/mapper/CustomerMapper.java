package com.denisborovkov.javacrm.customer.mapper;

import com.denisborovkov.javacrm.customer.dto.CreateCustomerRequest;
import com.denisborovkov.javacrm.customer.dto.CustomerDTO;
import com.denisborovkov.javacrm.customer.dto.UpdateCustomerRequest;
import com.denisborovkov.javacrm.customer.dto.UpdateCustomerResponse;
import com.denisborovkov.javacrm.customer.domain.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "id", source = "customerId")
    CustomerDTO toDTO(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "status", constant = "ACTIVE")
    Customer toEntity(CreateCustomerRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomer(UpdateCustomerRequest updateCustomerRequest,
                        @MappingTarget Customer entity);

    UpdateCustomerResponse toUpdateResponse(Customer customer);
}



