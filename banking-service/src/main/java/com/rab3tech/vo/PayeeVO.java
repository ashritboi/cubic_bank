package com.rab3tech.vo;

import java.sql.Timestamp;

import lombok.Data;

public @Data class PayeeVO {
	private int id;
	private String payeeAccountNo;
	private String payeeName;
	private String payeeNickName;
	private String customerId;
	private Timestamp doe;
	private Timestamp dom;
	private String remarks;
	private String status;
	private int urn;


}
