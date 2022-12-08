package com.example.demo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.example.demo.model.Customer} entity
 */
@Data
public class CustomerDto implements Serializable {
    private final String fullName;
    private final Set<AccountDto> accounts;
    private final String zipCode;
    private final Long phoneNumber;
    private final String address;
}