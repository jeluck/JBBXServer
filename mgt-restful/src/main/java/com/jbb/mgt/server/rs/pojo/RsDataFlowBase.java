package com.jbb.mgt.server.rs.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.DataFlowBase;
import com.jbb.server.shared.rs.Util;

/**
 * 流量基本信息response对象
 * 
 * @author wyq
 * @date 2018/04/28
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsDataFlowBase {
	private int dataFlowId;
	private String jexlScript;
	private String jexlDesc;
	private int price;
	private Long creationDate;
	private boolean isDelete;

	public RsDataFlowBase(DataFlowBase dfc) {
		this.dataFlowId=dfc.getDataFlowId();
		this.jexlScript=dfc.getJexlScript();
		this.jexlDesc=dfc.getJexlDesc();
		this.price=dfc.getPrice();
		this.creationDate=Util.getTimeMs(dfc.getCreationDate());
		this.isDelete=dfc.isDeleted();
	}
	
	public int getDataFlowId() {
		return dataFlowId;
	}

	public void setDataFlowId(int dataFlowId) {
		this.dataFlowId = dataFlowId;
	}

	public String getJexlScript() {
		return jexlScript;
	}

	public void setJexlScript(String jexlScript) {
		this.jexlScript = jexlScript;
	}

	public String getJexlDesc() {
		return jexlDesc;
	}

	public void setJexlDesc(String jexlDesc) {
		this.jexlDesc = jexlDesc;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public RsDataFlowBase(int dataFlowId, String jexlScript, String jexlDesc, int price, Long creationDate,
			boolean isDelete) {
		super();
		this.dataFlowId = dataFlowId;
		this.jexlScript = jexlScript;
		this.jexlDesc = jexlDesc;
		this.price = price;
		this.creationDate = creationDate;
		this.isDelete = isDelete;
	}

	public RsDataFlowBase() {
		super();
	}

	@Override
	public String toString() {
		return "RsDataFlowConfig [dataFlowId=" + dataFlowId + ", jexlScript=" + jexlScript + ", jexlDesc=" + jexlDesc
				+ ", price=" + price + ", creationDate=" + creationDate + ", isDelete=" + isDelete + "]";
	}

}
