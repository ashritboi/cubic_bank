package com.rab3tech.customer.service;

import java.util.List;

import com.rab3tech.vo.RequestTypeVO;

public interface CustomerRequestService {
	
	public List<RequestTypeVO> findAllRequests();
	public RequestTypeVO findById(int requestId);
	
	

}
