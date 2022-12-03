package com.example.demo.model;

import com.example.demo.enums.AccountType;
import com.example.demo.enums.Country;
import com.example.demo.enums.Currency;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Account {
    private Integer id;
    private int customerId;
    private Country country;
    private List<Currency> currencies;
    private AccountType accountType;
}