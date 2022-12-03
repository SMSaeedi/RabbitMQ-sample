package com.example.demo.dto;

import com.example.demo.enums.Currency;
import com.example.demo.enums.DirectionOfTransaction;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionResponse {
    private int accountId;
    private int transactionId;
    private BigDecimal amount;
    private Currency currency;
    private DirectionOfTransaction directionOfTransaction;
    private String description;
    private BigDecimal balanceAfterTransaction;
}