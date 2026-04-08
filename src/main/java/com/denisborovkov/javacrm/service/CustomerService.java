package com.denisborovkov.javacrm.service;

import com.denisborovkov.javacrm.dto.CreateCustomerRequest;
import com.denisborovkov.javacrm.dto.UpdateCustomerRequest;
import com.denisborovkov.javacrm.exception.CustomerNotFoundException;
import com.denisborovkov.javacrm.entity.Customer;
import com.denisborovkov.javacrm.mapper.CustomerMapper;
import com.denisborovkov.javacrm.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public Customer createCustomer(CreateCustomerRequest request) {
        return customerRepository.save(customerMapper.toEntity(request));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll().stream().toList();
    }

    public Customer getCustomer(Long id) throws CustomerNotFoundException {
        return customerRepository.findById(id).orElseThrow(()
                -> new CustomerNotFoundException(id));
    }

    public Customer getCustomer(String email) throws CustomerNotFoundException {
        if (customerRepository.findByEmail(email) == null) {
            throw new CustomerNotFoundException(email) ;
        }
        return customerRepository.findByEmail((email));
    }

    public Customer updateCustomer(Long id, UpdateCustomerRequest request)
            throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        customerMapper.updateCustomer(request, customer);
        return customerRepository.save(customer);
    }

    public void deleteCustomerById(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()
                -> new CustomerNotFoundException(id));
        customerRepository.delete(customer);
    }
}
