package com.example.demo.service;

import com.example.demo.dto.CustomerDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerDto createCustomer(String fullName, String zipCode, Long phoneNumber, String address) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Customer full name is required");
        }
        if (zipCode == null || zipCode.isBlank()) {
            throw new IllegalArgumentException("Customer zip code is required");
        }
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Customer phone number is required");
        }
        Customer customer = new Customer();
        customer.setFullName(fullName);
        customer.setZipCode(zipCode);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address == null ? "" : address);
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerDto.fromEntity(savedCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerDto findById(Long customerId) {
        return customerRepository.findById(customerId)
            .map(CustomerDto::fromEntity)
            .orElseThrow(() -> new NotFoundException("Customer with id %s not found".formatted(customerId)));
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> findAll() {
        return customerRepository.findAll().stream()
            .map(CustomerDto::fromEntity)
            .toList();
    }
}
