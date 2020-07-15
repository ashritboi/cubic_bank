package com.rab3tech.customer.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.Login;

public interface CustomerAccountInfoRepo extends JpaRepository<CustomerAccountInfo, Integer>{
	
	@Query("SELECT t FROM CustomerAccountInfo t where t.customerId = :puserid")
	Optional<CustomerAccountInfo> findAccountByEmail(@Param("puserid") Login customerId);
	
	@Query("SELECT t FROM CustomerAccountInfo t where t.customerId = :puserid")
	Optional<CustomerAccountInfo> findAccountByemail(@Param("puserid") String customerId);
	
	@Query("SELECT t FROM CustomerAccountInfo t where t.accountNumber = :accountNumber")
	Optional<CustomerAccountInfo> findbyAccountNumber(@Param("accountNumber") String accountNo);

}
