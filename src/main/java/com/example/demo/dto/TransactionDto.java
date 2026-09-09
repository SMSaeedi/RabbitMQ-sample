package com.example.demo.dto;

import com.example.demo.enums.TransactionType;
import com.example.demo.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(
    Long id,
    Long accountId,
    TransactionType type,
    BigDecimal amount,
    BigDecimal previousBalance,
    BigDecimal newBalance,
    String description,
    LocalDateTime createdAt
) {
    public static TransactionDto fromEntity(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return new TransactionDto(
            transaction.getId(),
            transaction.getAccount() != null ? transaction.getAccount().getId() : null,
            transaction.getType(),
            transaction.getAmount(),
            transaction.getPreviousBalance(),
            transaction.getNewBalance(),
            transaction.getDescription(),
            transaction.getCreatedAt()
        );
    }
}
