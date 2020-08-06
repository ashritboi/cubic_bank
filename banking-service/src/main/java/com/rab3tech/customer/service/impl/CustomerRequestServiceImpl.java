package com.rab3tech.customer.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.AccountStatusRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.RequestTypeRepository;
import com.rab3tech.customer.service.CustomerRequestService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerRequests;
import com.rab3tech.dao.entity.RequestType;
import com.rab3tech.vo.RequestTypeVO;

@Service
@Transactional
public class CustomerRequestServiceImpl implements CustomerRequestService{

	@Autowired
	RequestTypeRepository requestTypeRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	AccountStatusRepository accountStatusRepository;
	
	@Value("${bank.from.email:ashritboi@gmail.com}")
	private String fromEmail;
	
	@Override
	public List<RequestTypeVO> findAllRequests() {
		List<RequestTypeVO> requestVO = new ArrayList<RequestTypeVO>();
		List<RequestType> requestType = requestTypeRepository.findAll();
		for(RequestType request: requestType){
			if(request.getStatus() == 1){
			RequestTypeVO vo = new RequestTypeVO();
			BeanUtils.copyProperties(request, vo);
			requestVO.add(vo);
			}
		}
		return requestVO;
	}

	@Override
	public RequestTypeVO findById(int requestId) {
		RequestTypeVO vo = new RequestTypeVO();
		RequestType request = requestTypeRepository.findById(requestId).get();
		BeanUtils.copyProperties(request, vo);
		return vo;

	}
	
//	@Override
//	public String submitRquest(String email){
//		CustomerRequests customerRequests = new CustomerRequests();
//		Customer customer = customerRepository.findByEmail(email).get();
//		customerRequests.setCustomerId(customer);
//		return email;
//		
//	}

}
