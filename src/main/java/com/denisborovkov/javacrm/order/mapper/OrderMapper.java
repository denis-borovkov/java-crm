package com.denisborovkov.javacrm.order.mapper;

import com.denisborovkov.javacrm.order.dto.CreateOrderRequest;
import com.denisborovkov.javacrm.order.dto.OrderDTO;
import com.denisborovkov.javacrm.order.dto.UpdateOrderRequest;
import com.denisborovkov.javacrm.order.domain.Order;
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



