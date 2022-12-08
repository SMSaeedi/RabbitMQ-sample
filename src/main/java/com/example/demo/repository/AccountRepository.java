package com.example.demo.repository;

import com.example.demo.dto.AccountDto;
import com.example.demo.enums.AccountType;
import com.example.demo.enums.Currency;
import com.example.demo.model.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Mapper
public interface AccountRepository {
    @Insert("INSERT INTO accounts(id, customerId, accountType)" +
            " VALUES (#{id}, #{customerId}, #{currency}, #{accountType}")
    public Account insert(Integer id, Integer customerId, Currency currency, AccountType accountType);

    @Select("SELECT * FROM accounts WHERE id = #{id}")
    public Account findById(Integer id);

    @Select("SELECT * FROM accounts")
    public List<Account> findAll();
}