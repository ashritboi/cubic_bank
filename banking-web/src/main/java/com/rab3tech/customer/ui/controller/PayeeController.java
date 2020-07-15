package com.rab3tech.customer.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.service.impl.CustomerEnquiryService;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.ApplicationResponseVO;
import com.rab3tech.vo.PayeeVO;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/v3")
public class PayeeController {
	
	@Autowired
	private CustomerEnquiryService customerEnquiryService;
	
	@Autowired
	private PayeeInfoRepository payeeInfoRepository;
	
	@PostMapping("/customer/deletePayee")
	public ApplicationResponseVO deletePayee(@RequestBody PayeeVO payeeVO){
		
		boolean status = customerEnquiryService.emailNotExist(payeeVO.getCustomerId());
		ApplicationResponseVO applicationResponseVO = new ApplicationResponseVO();
		if(status){
			payeeInfoRepository.deleteById(payeeVO.getId());
			applicationResponseVO.setCode(200);
			applicationResponseVO.setMessage("Payee deleted....");
		}
		return applicationResponseVO;
	}

}
