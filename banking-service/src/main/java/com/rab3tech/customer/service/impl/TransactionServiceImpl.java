package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.AccountStatusRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepo;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.dao.repository.TransactionRepository;
import com.rab3tech.customer.service.AccountService;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.customer.service.TransactionService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.dao.entity.Transaction;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.TransactionVO;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private PayeeInfoService payeeInfoService;
	
	@Autowired
	private PayeeInfoRepository payeeInfoRepository;
	
	@Autowired
	private CustomerAccountInfoRepo accountRepository;
	
	@Autowired
	private CustomerRepository customerRepository; 

	@Override
	public String transferFund(TransactionVO transactionVO) {

		String message =null;
		
		Customer customer = customerRepository.findByEmail(transactionVO.getCustomerId()).get();
		Optional<CustomerAccountInfo>account = accountRepository.findAccountByEmail(customer.getLogin());
		CustomerAccountInfo debitAccount = new CustomerAccountInfo();
		if(account.isPresent()){
			debitAccount = account.get();
			if(!debitAccount.getAccountType().getCode().equalsIgnoreCase("AC001")){
				message = "You do not have saving account";
				return message;
			}
			
			if(debitAccount.getAvBalance()< transactionVO.getAmount()){
				message = "Account has insufficient balance";
				return message;
			}
			

			
		}else{
			message = "you do not have valid saving account";
			return message;
	}
	
	PayeeInfo payee = payeeInfoRepository.findById(transactionVO.getPayeeId()).get();
	Optional<CustomerAccountInfo> payeeAccount = accountRepository.findbyAccountNumber(payee.getPayeeAccountNo());
	
	float availableAmount = debitAccount.getAvBalance() - transactionVO.getAmount();
	debitAccount.setAvBalance(availableAmount);
	debitAccount.setTavBalance(availableAmount);
	accountRepository.save(debitAccount);
	
	CustomerAccountInfo creditAccount = payeeAccount.get();
	
	float avBalance = creditAccount.getAvBalance() + transactionVO.getAmount();
	creditAccount.setAvBalance(avBalance);
	creditAccount.setTavBalance(avBalance);
	accountRepository.save(creditAccount);
	
	Transaction transaction = new Transaction();
	transaction.setAmount(transactionVO.getAmount());
	transaction.setDebitAccountNumber(debitAccount.getAccountNumber());
	transaction.setDescription(transactionVO.getDescription());
	transaction.setPayeeId(payee);
	transaction.setTransactionDate(new Timestamp(1));
	transactionRepository.save(transaction);
	message = "Fund Transfer successful";
	
			return message;
	}
}
