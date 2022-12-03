package com.example.demo.model;

import com.example.demo.enums.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Balance {
    private BigDecimal availableAmount;
    private Currency currency;
}
