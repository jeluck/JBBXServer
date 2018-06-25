package com.jbb.mgt.server.rs.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.Roles;
import com.jbb.server.shared.rs.Util;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsAccount {

    private int accountId;
    private String username;
    private String nickname;
    private String phoneNumber;
    private Integer jbbUserId;
    private String password;
    private Integer orgId;
    private int creator;
    private Long creationDate;
    private boolean deleted;
    private boolean freeze;
    private List<Roles> roles;

    public RsAccount() {

    }

    public RsAccount(Account account) {
        this.accountId = account.getAccountId();
        this.username = account.getUsername();
        this.nickname = account.getNickname();
        this.phoneNumber = account.getPhoneNumber();
        this.jbbUserId = account.getJbbUserId();
        this.orgId = account.getOrgId();
        this.creator = account.getCreator();
        this.creationDate = Util.getTimeMs(account.getCreationDate());
        this.deleted = account.isDeleted();
        this.freeze = account.isFreeze();
        this.roles = account.getRoles();
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getJbbUserId() {
        return jbbUserId;
    }

    public void setJbbUserId(Integer jbbUserId) {
        this.jbbUserId = jbbUserId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }
}
