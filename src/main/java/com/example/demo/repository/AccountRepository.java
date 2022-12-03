package com.example.demo.repository;

import com.example.demo.dto.AccountResponse;
import com.example.demo.enums.AccountType;
import com.example.demo.enums.Currency;
import com.example.demo.model.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountRepository {
    @Insert("INSERT INTO accounts(id, customerId, accountType)"+
            " VALUES (#{id}, #{customerId}, #{currency}, #{accountType}")
    public AccountResponse insert(Integer id, Integer customerId, Currency currency, AccountType accountType);

    @Select("SELECT * FROM accounts WHERE id = #{id}")
    public Account findById(Integer id);
}