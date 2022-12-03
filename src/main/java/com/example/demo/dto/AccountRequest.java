package com.example.demo.dto;

import com.example.demo.enums.Country;
import com.example.demo.enums.Currency;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccountRequest {
    private int customerId;
    private Country country;
    private List<Currency> currencies;
}