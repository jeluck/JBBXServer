package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

/**
 * 流量基本信息实体类
 * 
 * @author wyq
 * @date 2018/04/26
 */
public class DataFlowBase {

    private int dataFlowId;
    private String jexlScript;
    private String jexlDesc;
    private int price;
    private Timestamp creationDate;
    private boolean deleted;

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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public int getDataFlowId() {
        return dataFlowId;
    }

    public void setDataFlowId(int dataFlowId) {
        this.dataFlowId = dataFlowId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "DataFlowConfig [dataFlowId=" + dataFlowId + ", jexlScript=" + jexlScript + ", jexlDesc=" + jexlDesc
            + ", price=" + price + ", creationDate=" + creationDate + ", deleted=" + deleted + "]";
    }

    public DataFlowBase() {
        super();
    }

    public DataFlowBase(int dataFlowId, String jexlScript, String jexlDesc, int price, Timestamp creationDate,
        boolean deleted) {
        super();
        this.dataFlowId = dataFlowId;
        this.jexlScript = jexlScript;
        this.jexlDesc = jexlDesc;
        this.price = price;
        this.creationDate = creationDate;
        this.deleted = deleted;
    }

}
