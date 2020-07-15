package com.rab3tech.customer.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepo;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.service.AccountService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.vo.CustomerAccountInfoVO;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerAccountInfoRepo customerAccountInfoRepository;

	@Override
	public Optional<CustomerAccountInfoVO> findByUserId(String customerId) {
		Customer customer = customerRepository.findByEmail(customerId).get();
		Optional<CustomerAccountInfo> account = customerAccountInfoRepository.findAccountByEmail(customer.getLogin());
		CustomerAccountInfoVO customerAccountInfoVO = new CustomerAccountInfoVO();
		if(account.isPresent()){
			BeanUtils.copyProperties(account.get(),customerAccountInfoVO, new String[]{"customerId", "accountType", "accountStatus"});
			customerAccountInfoVO.setStatusAsOf(account.get().getStatusAsOf()!=null?account.get().getStatusAsOf():null);
			customerAccountInfoVO.setAccountType(account.get().getAccountType()!=null?account.get().getAccountType().getCode():null);
			return Optional.of(customerAccountInfoVO);
		}else{
			return Optional.empty();
		}
	}
	
	

}
