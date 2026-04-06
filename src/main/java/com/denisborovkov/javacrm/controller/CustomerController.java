package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.CustomerDTO;
import com.denisborovkov.javacrm.mapper.CustomerMapper;
import com.denisborovkov.javacrm.entity.Customer;
import com.denisborovkov.javacrm.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @PostMapping("/create")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerService.createCustomer(
                customerDTO.firstName(),
                customerDTO.lastName(),
                customerDTO.email());
        return ResponseEntity.ok().body(customerMapper.toDTO(customer));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerDTO>> getAll() {
        return ResponseEntity.ok().body(customerService.getAllCustomers()
                .stream()
                .map(customerMapper::toDTO)
                .toList());
    }
}
