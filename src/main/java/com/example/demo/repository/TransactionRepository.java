package com.example.demo.repository;

import com.example.demo.dto.TransactionRequest;
import com.example.demo.dto.TransactionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TransactionRepository {
    @Update("Update accounts set amount=#{amount}, currency=#{currency}, " +
            "directionOfTransaction=#{directionOfTransaction}, " +
            "description=#{description}, " +
            "where id=#{accountId}")
    public TransactionResponse createTransaction(TransactionRequest request);
}