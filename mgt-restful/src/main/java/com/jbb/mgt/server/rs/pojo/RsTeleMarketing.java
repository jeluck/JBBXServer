package com.jbb.mgt.server.rs.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.TeleMarketing;
import com.jbb.server.shared.rs.Util;

/**
 * 批次查询response实体类
 * 
 * @author Administrator
 * @date 2018/04/29
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsTeleMarketing {

	private int batchId;
	private int accountId;
	private int cnt;
	private int validCnt;
	private int price;
	private int status;
	private Long creationDate;

	public RsTeleMarketing(TeleMarketing tm) {
		this.batchId=tm.getBatchId();
		this.accountId=tm.getAccountId();
		this.cnt=tm.getCnt();
		this.validCnt=tm.getValidCnt();
		this.price=tm.getPrice();
		this.status=tm.getStatus();
		this.creationDate=Util.getTimeMs(tm.getCreationDate());
	}
	
	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public int getValidCnt() {
		return validCnt;
	}

	public void setValidCnt(int validCnt) {
		this.validCnt = validCnt;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	public RsTeleMarketing() {
		super();
	}

	@Override
	public String toString() {
		return "RsTeleMarketing [batchId=" + batchId + ", accountId=" + accountId + ", cnt=" + cnt + ", validCnt="
				+ validCnt + ", price=" + price + ", status=" + status + ", creationDate=" + creationDate + "]";
	}

}
