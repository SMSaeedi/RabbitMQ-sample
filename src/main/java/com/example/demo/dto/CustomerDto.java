package com.example.demo.dto;

import com.example.demo.model.Customer;

import java.util.List;

public record CustomerDto(Long id, String fullName, String zipCode, Long phoneNumber, String address, List<AccountDto> accounts) {
    public static CustomerDto fromEntity(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerDto(
            customer.getId(),
            customer.getFullName(),
            customer.getZipCode(),
            customer.getPhoneNumber(),
            customer.getAddress(),
            customer.getAccounts() == null
                ? List.of()
                : customer.getAccounts().stream().map(AccountDto::fromEntity).toList()
        );
    }
}
