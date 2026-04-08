package com.denisborovkov.javacrm.service;

import com.denisborovkov.javacrm.exception.CustomerNotFoundException;
import com.denisborovkov.javacrm.entity.Customer;
import com.denisborovkov.javacrm.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer createCustomer(String firstName, String lastName, String email) {
        Customer customer = Customer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
        return customerRepository.save(customer);
    }

    public Customer setDescription(Long id, String description) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()
                -> new  CustomerNotFoundException(id));
        customer.setDescription(description);
        return customerRepository.save(customer);
    }

    public String getDescription(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()
                -> new CustomerNotFoundException(id));
        return customer.getDescription();
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
}
