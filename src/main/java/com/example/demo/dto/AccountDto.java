package com.example.demo.dto;

import com.example.demo.enums.AccountType;
import com.example.demo.enums.Currency;
import com.example.demo.model.Account;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link Account} entity
 */
@Builder
@Data
public class AccountDto implements Serializable {
    private final BigDecimal amount;
    private final Currency currency;
    private final AccountType accountType;
}