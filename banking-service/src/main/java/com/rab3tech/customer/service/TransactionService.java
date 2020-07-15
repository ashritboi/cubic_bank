package com.rab3tech.customer.service;

import java.util.List;

import com.rab3tech.vo.TransactionVO;

public interface TransactionService {
	
	public String transferFund(TransactionVO transactionVO);
	List<TransactionVO> getAllTransactions(String userId);

}
