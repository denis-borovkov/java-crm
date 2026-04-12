package com.denisborovkov.javacrm.service;

import com.denisborovkov.javacrm.dto.entity.CreateCustomerRequest;
import com.denisborovkov.javacrm.dto.entity.CustomerDTO;
import com.denisborovkov.javacrm.dto.entity.UpdateCustomerRequest;
import com.denisborovkov.javacrm.exception.customer.CustomerNotFoundException;
import com.denisborovkov.javacrm.entity.Customer;
import com.denisborovkov.javacrm.mapper.CustomerMapper;
import com.denisborovkov.javacrm.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        Customer customer = customerRepository.save(customerMapper.toEntity(request));
        return customerMapper.toDTO(customer);
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::toDTO).toList();
    }

    public CustomerDTO getCustomerById(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()
                -> new CustomerNotFoundException(id));
        return customerMapper.toDTO(customer);
    }

    public CustomerDTO getCustomerByEmail(String email) throws CustomerNotFoundException {
        Customer customer = customerRepository.findByEmail(email).orElseThrow(()
                -> new CustomerNotFoundException(email));
        return customerMapper.toDTO(customer);
    }

    @Transactional
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
