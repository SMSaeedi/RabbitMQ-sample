package com.example.demo.service;

import com.example.demo.dto.TransactionDto;
import com.example.demo.enums.TransactionType;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Account;
import com.example.demo.model.Transaction;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionDto credit(Long accountId, BigDecimal amount, String description) {
        Account account = findAccount(accountId);
        BigDecimal normalizedAmount = validateAmount(amount);
        BigDecimal previousBalance = account.getBalance();
        BigDecimal newBalance = previousBalance.add(normalizedAmount);

        account.setBalance(newBalance);
        Transaction transaction = new Transaction(
            account,
            TransactionType.CREDIT,
            normalizedAmount,
            previousBalance,
            newBalance,
            description == null ? "Credit" : description
        );

        Transaction savedTransaction = transactionRepository.save(transaction);
        account.getTransactions().add(savedTransaction);
        accountRepository.save(account);
        return TransactionDto.fromEntity(savedTransaction);
    }

    @Transactional
    public TransactionDto debit(Long accountId, BigDecimal amount, String description) {
        Account account = findAccount(accountId);
        BigDecimal normalizedAmount = validateAmount(amount);
        BigDecimal previousBalance = account.getBalance();

        if (previousBalance.compareTo(normalizedAmount) < 0) {
            throw new IllegalArgumentException("Insufficient funds for account %s".formatted(accountId));
        }

        BigDecimal newBalance = previousBalance.subtract(normalizedAmount);
        account.setBalance(newBalance);
        Transaction transaction = new Transaction(
            account,
            TransactionType.DEBIT,
            normalizedAmount,
            previousBalance,
            newBalance,
            description == null ? "Debit" : description
        );

        Transaction savedTransaction = transactionRepository.save(transaction);
        account.getTransactions().add(savedTransaction);
        accountRepository.save(account);
        return TransactionDto.fromEntity(savedTransaction);
    }

    @Transactional(readOnly = true)
    public TransactionDto getById(Long transactionId) {
        return transactionRepository.findById(transactionId)
            .map(TransactionDto::fromEntity)
            .orElseThrow(() -> new NotFoundException("Transaction with id %s not found".formatted(transactionId)));
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getHistory(Long accountId, Integer limit, LocalDate fromDate, LocalDate toDate) {
        findAccount(accountId);
        int pageLimit = limit == null || limit <= 0 ? 10 : limit;

        if (fromDate != null || toDate != null) {
            LocalDateTime start = fromDate == null ? LocalDate.of(1970, 1, 1).atStartOfDay() : fromDate.atStartOfDay();
            LocalDateTime end = toDate == null ? LocalDate.now().atTime(LocalTime.MAX) : toDate.atTime(LocalTime.MAX);
            return transactionRepository.findByAccountIdAndCreatedAtBetweenOrderByCreatedAtDesc(accountId, start, end, PageRequest.of(0, pageLimit))
                .stream()
                .map(TransactionDto::fromEntity)
                .toList();
        }

        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId, PageRequest.of(0, pageLimit))
            .stream()
            .map(TransactionDto::fromEntity)
            .toList();
    }

    private Account findAccount(Long accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new NotFoundException("Account with id %s not found".formatted(accountId)));
    }

    private BigDecimal validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
