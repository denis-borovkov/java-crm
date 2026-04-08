package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.CreateCustomerRequest;
import com.denisborovkov.javacrm.dto.CustomerDTO;
import com.denisborovkov.javacrm.exception.CustomerNotFoundException;
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
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CreateCustomerRequest request) {
        Customer customer = customerService.createCustomer(
                request.firstName(),
                request.lastName(),
                request.email());
        return ResponseEntity.ok().body(customerMapper.toDTO(customer));
    }

    @PostMapping("/descripion/add/{id}")
    public ResponseEntity<CustomerDTO> addDescription (@PathVariable Long id, String description)
            throws CustomerNotFoundException {
        Customer customer = customerService.setDescription(id, description);
        return ResponseEntity.ok().body(customerMapper.toDTO(customer));
    }

    @GetMapping("/description")
    public ResponseEntity<String> getDescription(@RequestParam(required = false) Long id)
            throws CustomerNotFoundException {
        String description = customerService.getDescription(id);
        return ResponseEntity.ok().body(description);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id)
            throws CustomerNotFoundException {
        Customer customer = customerService.getCustomer(id);
        return ResponseEntity.ok().body(customerMapper.toDTO(customer));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email)
            throws CustomerNotFoundException {
        Customer customer = customerService.getCustomer(email);
        return ResponseEntity.ok().body(customerMapper.toDTO(customer));
    }

    @GetMapping()
    public ResponseEntity<List<CustomerDTO>> getAll() {
        return ResponseEntity.ok().body(customerService.getAllCustomers()
                .stream()
                .map(customerMapper::toDTO)
                .toList());
    }
}
