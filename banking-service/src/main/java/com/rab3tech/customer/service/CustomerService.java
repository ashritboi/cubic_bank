package com.rab3tech.customer.service;

import java.util.Optional;

import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerVO;

public interface CustomerService {

	CustomerVO createAccount(CustomerVO customerVO);
	CustomerAccountInfoVO createBankAccount(int csaid);
	CustomerVO editAccount(String email);
	void update(CustomerVO customerVO);


}
