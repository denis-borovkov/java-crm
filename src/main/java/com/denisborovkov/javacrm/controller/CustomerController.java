package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.CreateCustomerRequest;
import com.denisborovkov.javacrm.dto.CustomerDTO;
import com.denisborovkov.javacrm.dto.UpdateCustomerRequest;
import com.denisborovkov.javacrm.dto.UpdateCustomerResponse;
import com.denisborovkov.javacrm.exception.CustomerNotFoundException;
import com.denisborovkov.javacrm.mapper.CustomerMapper;
import com.denisborovkov.javacrm.entity.Customer;
import com.denisborovkov.javacrm.service.CustomerService;
import jakarta.validation.Valid;
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
        Customer customer = customerService.createCustomer(request);
        return ResponseEntity.ok().body(customerMapper.toDTO(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id)
            throws CustomerNotFoundException {
        Customer customer = customerService.getCustomer(id);
        return ResponseEntity.ok().body(customerMapper.toDTO(customer));
    }

    @GetMapping("/{email}")
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

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateCustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request)
            throws CustomerNotFoundException {
        Customer updatedCustomer = customerService.updateCustomer(id, request);
        return ResponseEntity.ok().body(customerMapper.toUpdateResponse(updatedCustomer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id)
            throws CustomerNotFoundException {
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }
}
