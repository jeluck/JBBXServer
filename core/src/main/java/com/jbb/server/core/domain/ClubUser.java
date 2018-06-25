package com.jbb.server.core.domain;

import java.sql.Timestamp;

/**
 * 
 * @author VincentTang
 * @date 2017年12月21日
 */

public class ClubUser {
	
	private String mobile;
    private String idcardNo;
    private String userName;
    private String serverPass;
    private Timestamp creationDate;
    private Timestamp updateDate;
    private int userId;
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdcardNo() {
		return idcardNo;
	}

	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServerPass() {
		return serverPass;
	}

	public void setServerPass(String serverPass) {
		this.serverPass = serverPass;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}




	public int getUserId() {
		return userId;
	}




	public void setUserId(int userId) {
		this.userId = userId;
	}



    


 
    @Override
    public String toString() {
        return "ClubUser [mobile=" + mobile + ", idcardNo=" + idcardNo + ", userName=" + userName + ", serverPass="
            + serverPass + ", creationDate=" + creationDate + ", updateDate=" + updateDate + ", userId=" + userId +"]";
    }

}
