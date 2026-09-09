package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import com.example.demo.enums.AccountType;
import com.example.demo.enums.Currency;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Account;
import com.example.demo.model.Customer;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepository, customerRepository);
    }

    @Test
    void createAccount_shouldPersistTheAccountForAValidCustomer() {
        Customer customer = new Customer();
        customer.setId(10L);
        customer.setFullName("Jane Doe");
        customer.setZipCode("10001");
        customer.setPhoneNumber(555123456L);
        customer.setAddress("Main Street 7");

        Account savedAccount = new Account();
        savedAccount.setId(99L);
        savedAccount.setCustomer(customer);
        savedAccount.setBalance(new BigDecimal("2500.75"));
        savedAccount.setCurrency(Currency.USD);
        savedAccount.setAccountType(AccountType.SALARY);

        when(customerRepository.findById(10L)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        AccountDto accountDto = accountService.createAccount(10L, new BigDecimal("2500.75"), Currency.USD, AccountType.SALARY);

        assertThat(accountDto.id()).isEqualTo(99L);
        assertThat(accountDto.customerId()).isEqualTo(10L);
        assertThat(accountDto.balance()).isEqualByComparingTo(new BigDecimal("2500.75"));
        assertThat(accountDto.currency()).isEqualTo(Currency.USD);
        assertThat(accountDto.accountType()).isEqualTo(AccountType.SALARY);
    }

    @Test
    void createAccount_shouldThrowWhenCustomerDoesNotExist() {
        when(customerRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.createAccount(404L, BigDecimal.TEN, Currency.EUR, AccountType.SAVINGS))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Customer with id 404 not found");
    }
}
