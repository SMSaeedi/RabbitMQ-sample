package com.example.demo.dto;

import com.example.demo.enums.AccountType;
import com.example.demo.enums.Currency;
import com.example.demo.model.Account;

import java.math.BigDecimal;

public record AccountDto(Long id, Long customerId, BigDecimal balance, Currency currency, AccountType accountType) {
    public static AccountDto fromEntity(Account account) {
        if (account == null) {
            return null;
        }
        return new AccountDto(
            account.getId(),
            account.getCustomer() != null ? account.getCustomer().getId() : null,
            account.getBalance(),
            account.getCurrency(),
            account.getAccountType()
        );
    }
}
