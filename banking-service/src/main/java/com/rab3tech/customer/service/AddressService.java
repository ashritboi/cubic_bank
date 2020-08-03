package com.rab3tech.customer.service;

import com.rab3tech.vo.AddressVO;

public interface AddressService {
	public String saveAddress(AddressVO address);
	public AddressVO getAddressByUserId(String userId);

}
