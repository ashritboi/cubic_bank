package com.rab3tech.admin.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerQuestionAnswer;

/**
 * 
 * @author nagendra
 * 
 * 
 * Spring Data JPA repository
 * 
 * 
 * 
 */
public interface MagicCustomerRepository extends JpaRepository<Customer, Integer> {
	public Optional<Customer> findByEmail(String email);
	
	@Query("UPDATE Customer c SET c.address = :address, c.jobTitle = :jobTitle,"
			+ " c.mobile = :mobile, c.qualification = :qualification  WHERE c.email = :email")
	public void update(@Param("address") String address,@Param("jobTitle") String jobTitle,@Param("mobile") String mobile,@Param("qualification") String qualification,@Param("email") String email);
	
}

