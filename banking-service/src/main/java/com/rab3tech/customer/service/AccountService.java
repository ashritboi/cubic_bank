package com.rab3tech.customer.service;

import java.util.Optional;

import com.rab3tech.vo.CustomerAccountInfoVO;

public interface AccountService {

	
	public Optional<CustomerAccountInfoVO> findByUserId(String customerId);
}
