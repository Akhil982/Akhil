package com.capg.ow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capg.ow.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Integer>{

}
