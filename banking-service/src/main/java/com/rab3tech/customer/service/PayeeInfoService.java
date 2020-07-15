package com.rab3tech.customer.service;

import java.util.List;
import java.util.Optional;

import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.PayeeVO;

public interface PayeeInfoService {
public String save(PayeeVO payeeVO);

List<PayeeInfo> findPayee();

Optional<PayeeVO> findPayeeData(String id);

public void deletePayee(int id);

PayeeVO editPayee(String email);

PayeeVO update(PayeeVO payeeVO);


//void update(PayeeVO payeeVO);

}
