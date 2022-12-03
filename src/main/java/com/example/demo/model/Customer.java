package com.example.demo.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Customer {
    private Integer id;
    private String fullName;
    private List<Account> accounts;
    private Long phoneNr;
    private String postalAddress;
}