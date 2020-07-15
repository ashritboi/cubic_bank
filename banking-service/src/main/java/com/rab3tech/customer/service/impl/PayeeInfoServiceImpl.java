package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.aop.advice.TimeLogger;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepo;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.service.AccountService;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.CustomerSaving;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.utils.AccountStatusEnum;
import com.rab3tech.utils.Utils;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerSavingVO;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.PayeeVO;


@Service
@Transactional
public class PayeeInfoServiceImpl implements PayeeInfoService{

	
	@Autowired
	private PayeeInfoRepository payeeInfoRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CustomerAccountInfoRepo customerAccountInfoRepo;
	
	
	
	@Override
	public String save(PayeeVO payeeVO) {
		String message = null;
		
 
//		
//		if(!customerAccount.isPresent()){
//			message = "you do not have valid saving account";
//			return message;
//		}
		
		Optional<PayeeInfo>payee = payeeInfoRepository.findByNickName(payeeVO.getCustomerId(),payeeVO.getPayeeNickName());
		if(payee.isPresent()){
			message = "Payee with same NickName already exists";
			return message;
		}
		
		
		
		//Optional<PayeeInfo>payee1 = payeeInfoRepository.findByEmail(payeeVO.getCustomerId());
//		if(payee1.isPresent()){
//			message = "Payee with same Account Number already exists";
//			return message;
//		}
		
//		Optional<PayeeInfo>payee2 = payeeInfoRepository.findByAccountId(payeeVO.getCustomerId(),payeeVO.getPayeeAccountNo());
//		if(payee2.isPresent()){
//			message = "Payee with same Account Id already exists";
//			return message;
//		} 
		
//		Optional<CustomerAccountInfo >payeeAccount = customerAccountInfoRepo.findAccountByemail(payeeVO.getCustomerId());
//		if(payeeInfoRepository.findByEmail(payeeVO.getCustomerId());
		
//		if(payee2.get().getStatus().equalsIgnoreCase("AS04")){
//			message = "Payee Status not valid";
//			return message;
//
//		}
		//Optional<CustomerAccountInfo >payeeAccount = customerAccountInfoRepo.findAccountByEmail(payeeVO);
//		List<PayeeInfo>payee3 = payeeInfoRepository.findAll();
//		if(payee1.get().getPayeeAccountNo().equalsIgnoreCase(payeeVO.getPayeeAccountNo())){
//			message = "Payee with same account number already exists";
//			return message;
//		};
//		
//		CustomerAccountInfo customerAccountInfo = payeeAccount.get();
//		if(payee2.get().getPayeeAccountNo().equalsIgnoreCase(payeeVO.getPayeeAccountNo())){
//			message = "You cannot add yourself as a payee";
//			return message;
//		}
	
			
		PayeeInfo payeeInfo = new PayeeInfo();
		BeanUtils.copyProperties(payeeVO, payeeInfo);
		payeeInfo.setDoe(new Timestamp(new Date().getTime()));
		payeeInfo.setDom(new Timestamp(new Date().getTime()));
		payeeInfo.setStatus("AS04");
		payeeInfoRepository.save(payeeInfo);
		message="Payee details has been saved successfully";
		return message;
	}
	
	@Override
	public List<PayeeInfo> findPayee() {
		List<PayeeInfo> payeeList = payeeInfoRepository.findAll();
		return payeeList;
	}
	
//	@Override
//	public void update(int id) {
//		PayeeInfo payeeInfo = payeeInfoRepository.findById(id).get();
//		PayeeVO payeeVO1 = new PayeeVO();
//		BeanUtils.copyProperties(payeeInfo, payeeVO1);
//		//return payeeVO1;
//		}

	@Override
	public void deletePayee(int id) {
		payeeInfoRepository.deleteById(id);
	}

	@Override
	public Optional<PayeeVO> findPayeeData(String id) {
		
		PayeeVO payeeVO =new PayeeVO();
		//payeeVO.setId(id);
		Optional<PayeeInfo>  optional=payeeInfoRepository.findBypayeeAccountNo(id);
		if(optional.isPresent()) {
			PayeeInfo payee=optional.get();
			payeeVO.setPayeeAccountNo(payee.getPayeeAccountNo());
			payeeVO.setPayeeName(payee.getPayeeName());
			payeeVO.setPayeeNickName(payee.getPayeeNickName());
			payeeVO.setRemarks(payee.getRemarks());
				
			return Optional.of(payeeVO);
		}else {
			return Optional.empty();
		}
	}
	

	@Override
	public PayeeVO editPayee(String email) {
		//PayeeInfo payee = payeeInfoRepository.findBycustomerId(email).get();
		PayeeVO payeeVO = new PayeeVO();
		//BeanUtils.copyProperties(payee, payeeVO);
		return payeeVO;
		}
	
	@Override
	public PayeeVO update(PayeeVO payeeVO) {
		//PayeeVO payeeVO =new PayeeVO();
		PayeeInfo payeeInfo =new PayeeInfo();
		if(payeeInfoRepository.findBypayeeAccountNo(payeeVO.getPayeeAccountNo()).isPresent())
			payeeInfo = payeeInfoRepository.findBypayeeAccountNo(payeeVO.getPayeeAccountNo()).get();
		payeeInfo.setPayeeAccountNo(payeeVO.getPayeeAccountNo());
		payeeInfo.setPayeeName(payeeVO.getPayeeName());
		payeeInfo.setPayeeNickName(payeeVO.getPayeeNickName());
		payeeInfo.setRemarks(payeeVO.getRemarks());
		

		
		return payeeVO;

		}
	
	
	

}
