package com.rab3tech.customer.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
	@Query("select t from transaction where t.debitAccountNumber = :accountNumber or t.payeeId.payeeAccountNo =:accountNumber order by t.transactionDate")
	List<Transaction> getAllTransactions(@Param("accountNumber")String accountNumber);

}
