package com.rab3tech.customer.service;

import java.util.List;
import java.util.Optional;

import com.rab3tech.vo.CustomerAccountInfoVO;

public interface AccountService {

	public void createAccount(String customerId);
	List<CustomerAccountInfoVO> findAll();
	public Optional<CustomerAccountInfoVO> findByUserId(String customerId);
	List<CustomerAccountInfoVO> findPendingAccounts();
}
