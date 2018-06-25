package com.jbb.server.core.domain;

import java.util.Date;

public class UserNoteRecord {
	/**
	 * 用户与借条关系Id
	 */
	private int userNoteRecordId;
	/**
	 * 借条记录Id
	 */
	private int loanNoteId;
	/**
	 * 借款人Id
	 */
	private int loanUserId;
	/**
	 * 出借人Id
	 */
	private int lenderId;
	/**
	 * 借款金额
	 */
	private float borrowingAmount;
	/**
	 * 借款日期
	 */
	private Date borrowingDate;
	/**
	 * 还款日期
	 */
	private Date repaymentDate;
	/**
	 * 延期还款时间
	 */
	private Date deferredDate;
	/**
	 * 年化利率
	 */
	private float annualRate;
	/**
	 * 借款用途
	 */
	private String borrowingPurpose;
	/**
	 * 借条创建日期
	 */
	private Date creationDate;
	/**
	 * 借条生效日期
	 */
	private Date userfulDate;
	/**
	 * 借条状态
	 */
	private String status;
	/**
	 * 是否删除
	 */
	private String isDeleted;
	
	
}
