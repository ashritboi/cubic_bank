package com.rab3tech.customer.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rab3tech.customer.dao.repository.AddressRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.service.AddressService;
import com.rab3tech.dao.entity.Address;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.vo.AddressVO;

@Service
@Transactional
public class AddressServiceImpl implements AddressService{
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	LoginRepository loginRepository;

	@Override
	public String saveAddress(AddressVO addressVO) {
		String message= null;
		Address address = new Address();
		Optional<Login>  optional=loginRepository.findByLoginid(addressVO.getLogonId());
		BeanUtils.copyProperties(addressVO, address);
		address.setLogonId(optional.get());
		addressRepository.save(address);
		return message;
	}

	@Override
	public AddressVO getAddressByUserId(String userId) {
		AddressVO addressVO = new AddressVO();
		Optional<Login> optional = loginRepository.findByLoginid(userId);
		Optional<Address> address = addressRepository.findByLogonId(optional.get());
		BeanUtils.copyProperties(address, addressVO);
		return null;
	}

}
