package com.example.demo.service;

import com.example.demo.dto.TransactionDto;
import com.example.demo.enums.TransactionType;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Account;
import com.example.demo.model.Customer;
import com.example.demo.model.Transaction;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(accountRepository, transactionRepository);
    }

    @Test
    void credit_shouldIncreaseBalanceAndCreateCreditTransaction() {
        Customer customer = new Customer();
        customer.setId(1L);

        Account account = new Account();
        account.setId(7L);
        account.setCustomer(customer);
        account.setBalance(new BigDecimal("100.00"));

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(11L);
        savedTransaction.setAccount(account);
        savedTransaction.setType(TransactionType.CREDIT);
        savedTransaction.setAmount(new BigDecimal("50.00"));
        savedTransaction.setPreviousBalance(new BigDecimal("100.00"));
        savedTransaction.setNewBalance(new BigDecimal("150.00"));

        when(accountRepository.findById(7L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionDto result = transactionService.credit(7L, new BigDecimal("50.00"), "Salary");

        assertThat(result.id()).isEqualTo(11L);
        assertThat(result.type()).isEqualTo(TransactionType.CREDIT);
        assertThat(result.amount()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(result.newBalance()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("150.00"));
    }

    @Test
    void debit_shouldDecreaseBalanceAndCreateDebitTransaction() {
        Customer customer = new Customer();
        customer.setId(2L);

        Account account = new Account();
        account.setId(8L);
        account.setCustomer(customer);
        account.setBalance(new BigDecimal("200.00"));

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(12L);
        savedTransaction.setAccount(account);
        savedTransaction.setType(TransactionType.DEBIT);
        savedTransaction.setAmount(new BigDecimal("40.00"));
        savedTransaction.setPreviousBalance(new BigDecimal("200.00"));
        savedTransaction.setNewBalance(new BigDecimal("160.00"));

        when(accountRepository.findById(8L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionDto result = transactionService.debit(8L, new BigDecimal("40.00"), "Rent");

        assertThat(result.id()).isEqualTo(12L);
        assertThat(result.type()).isEqualTo(TransactionType.DEBIT);
        assertThat(result.newBalance()).isEqualByComparingTo(new BigDecimal("160.00"));
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("160.00"));
    }

    @Test
    void debit_shouldRejectWhenInsufficientFunds() {
        Account account = new Account();
        account.setId(9L);
        account.setBalance(new BigDecimal("20.00"));

        when(accountRepository.findById(9L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> transactionService.debit(9L, new BigDecimal("25.00"), "Bill"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Insufficient funds for account 9");
    }

    @Test
    void getHistory_shouldReturnTransactionsForAccountWithinDateRange() {
        Account account = new Account();
        account.setId(15L);
        account.setBalance(new BigDecimal("500.00"));

        when(accountRepository.findById(15L)).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccountIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            eq(15L),
            any(),
            any(),
            any()
        )).thenReturn(List.of(new Transaction()));

        List<TransactionDto> result = transactionService.getHistory(15L, 5, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        assertThat(result).hasSize(1);
    }

    @Test
    void getById_shouldThrowWhenTransactionNotFound() {
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getById(99L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Transaction with id 99 not found");
    }
}
