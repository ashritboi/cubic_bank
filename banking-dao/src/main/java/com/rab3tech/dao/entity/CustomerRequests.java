package com.rab3tech.dao.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author nagendra.yadav
 * 00051230383783
 * 
 */
@Entity
@Table(name="customer_requests_tbl")
public class CustomerRequests {

	private long id;
	private Login customerId;
	private String reqRefNumber;
	private Address addressId;
	private RequestType requestType;
	private AccountStatus status;
	private float avBalance;
	private Date doe;
	private Date doa;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Login getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Login customerId) {
		this.customerId = customerId;
	}
	public String getReqRefNumber() {
		return reqRefNumber;
	}
	public void setReqRefNumber(String reqRefNumber) {
		this.reqRefNumber = reqRefNumber;
	}
	public Address getAddressId() {
		return addressId;
	}
	public void setAddressId(Address addressId) {
		this.addressId = addressId;
	}
	public RequestType getRequestType() {
		return requestType;
	}
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "status", nullable = false)
	public AccountStatus getStatus() {
		return status;
	}
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
	public float getAvBalance() {
		return avBalance;
	}
	public void setAvBalance(float avBalance) {
		this.avBalance = avBalance;
	}
	public Date getDoe() {
		return doe;
	}
	public void setDoe(Date doe) {
		this.doe = doe;
	}
	public Date getDoa() {
		return doa;
	}
	public void setDoa(Date doa) {
		this.doa = doa;
	}
	@Override
	public String toString() {
		return "CustomerRequests [id=" + id + ", customerId=" + customerId + ", reqRefNumber=" + reqRefNumber
				+ ", addressId=" + addressId + ", requestType=" + requestType + ", status=" + status + ", avBalance="
				+ avBalance + ", doe=" + doe + ", doa=" + doa + "]";
	}
	
	
	
}

