package com.denisborovkov.javacrm.mapper;

import com.denisborovkov.javacrm.dto.entity.CreateOrderRequest;
import com.denisborovkov.javacrm.dto.entity.OrderDTO;
import com.denisborovkov.javacrm.dto.entity.UpdateOrderRequest;
import com.denisborovkov.javacrm.entity.Order;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toDTO(Order order);
    Order toEntity(CreateOrderRequest orderDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOrder(UpdateOrderRequest updateOrderRequest,
                        @MappingTarget Order order);

}
