package com.denisborovkov.javacrm.controller;

import com.denisborovkov.javacrm.dto.entity.CreateCustomerRequest;
import com.denisborovkov.javacrm.dto.entity.CustomerDTO;
import com.denisborovkov.javacrm.dto.entity.UpdateCustomerRequest;
import com.denisborovkov.javacrm.dto.entity.UpdateCustomerResponse;
import com.denisborovkov.javacrm.exception.customer.CustomerNotFoundException;
import com.denisborovkov.javacrm.mapper.CustomerMapper;
import com.denisborovkov.javacrm.entity.Customer;
import com.denisborovkov.javacrm.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @PostMapping("/create")
    public CustomerDTO create(@RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @GetMapping("/id/{id}")
    public CustomerDTO get(@PathVariable Long id) throws CustomerNotFoundException {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/{email}")
    public CustomerDTO get(@PathVariable String email) throws CustomerNotFoundException {
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping()
    public List<CustomerDTO> getAll() {
        return customerService.getAllCustomers();
    }

    @PatchMapping("/update/{id}")
    public UpdateCustomerResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) throws CustomerNotFoundException {
        Customer updatedCustomer = customerService.updateCustomer(id, request);
        return customerMapper.toUpdateResponse(updatedCustomer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws CustomerNotFoundException {
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }
}
