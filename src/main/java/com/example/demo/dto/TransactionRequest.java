package com.example.demo.dto;

import com.example.demo.enums.Currency;
import com.example.demo.enums.DirectionOfTransaction;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionRequest {
    private int accountId;
    private BigDecimal amount;
    private Currency currency;
    private DirectionOfTransaction directionOfTransaction;
    private String description;
}
