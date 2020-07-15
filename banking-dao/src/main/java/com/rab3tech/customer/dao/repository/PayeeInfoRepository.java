package com.rab3tech.customer.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.PayeeInfo;

public interface PayeeInfoRepository extends JpaRepository<PayeeInfo, Integer>{
	
	@Query("SELECT t FROM PayeeInfo t where t.customerId = :puserid ")
	Optional<PayeeInfo> findByEmail(@Param("puserid") String customerId );

	@Query("SELECT t FROM PayeeInfo t where t.customerId = :puserid and t.payeeNickName = :pnickName")
	Optional<PayeeInfo> findByNickName(@Param("puserid") String customerId, @Param("pnickName") String payeeNickName);
	
	@Query("SELECT t FROM PayeeInfo t where t.customerId = :puserid and t.payeeAccountNo = :pAccountNumber")
	Optional<PayeeInfo> findByAccountId(@Param("puserid") String customerId, @Param("pAccountNumber") String payeeAccountNo);
	
	@Query("SELECT t FROM PayeeInfo t where t.id = :id ")
	Optional<PayeeInfo> findByUid(@Param("id") int id );


	@Query("SELECT t FROM PayeeInfo t where t.payeeAccountNo = :payeeAccountNo ")
	public Optional<PayeeInfo> findBypayeeAccountNo(@Param("payeeAccountNo") String payeeAccountNo);
	
	
	@Query("UPDATE PayeeInfo c SET c.payeeAccountNo = :payeeAccountNo, c.payeeName = :payeeName,"
			+ " c.payeeNickName = :payeeNickName, c.remarks = :remarks  WHERE c.customerId = :customerId")
	public void update(@Param("payeeAccountNo") String address,@Param("payeeName") String jobTitle,@Param("payeeNickName") String mobile,@Param("remarks") String qualification,@Param("customerId") String customerId);
	
	
	
	
}
