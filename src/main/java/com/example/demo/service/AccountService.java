package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import com.example.demo.enums.AccountType;
import com.example.demo.enums.Currency;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Account;
import com.example.demo.model.Customer;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public AccountDto createAccount(Long customerId, BigDecimal balance, Currency currency, AccountType accountType) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new NotFoundException("Customer with id %s not found".formatted(customerId)));

        Account account = new Account();
        account.setCustomer(customer);
        account.setBalance(balance == null ? BigDecimal.ZERO : balance);
        account.setCurrency(currency == null ? Currency.USD : currency);
        account.setAccountType(accountType == null ? AccountType.SAVINGS : accountType);

        Account savedAccount = accountRepository.save(account);
        customer.getAccounts().add(savedAccount);
        return AccountDto.fromEntity(savedAccount);
    }

    @Transactional(readOnly = true)
    public AccountDto getById(Long accountId) {
        return accountRepository.findById(accountId)
            .map(AccountDto::fromEntity)
            .orElseThrow(() -> new NotFoundException("Account with id %s not found".formatted(accountId)));
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new NotFoundException("Customer with id %s not found".formatted(customerId)));
        return customer.getAccounts().stream()
            .map(AccountDto::fromEntity)
            .toList();
    }
}
